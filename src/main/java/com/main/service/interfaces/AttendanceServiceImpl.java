package com.main.service.interfaces;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.AttendanceDTO;
import com.main.dto.AttendanceInputDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.model.Attendance;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.repository.AttendanceRepository;
import com.main.service.implementations.AfterSchoolCareService;
import com.main.service.implementations.AttendanceService;
import com.main.service.implementations.UserService;
import org.aspectj.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private AttendanceRepository repository;
    private AfterSchoolCareService afterSchoolCareService;
    private UserService userService;

    public AttendanceServiceImpl(AttendanceRepository repository, AfterSchoolCareService afterSchoolCareService, UserService userService) {
        this.repository = repository;
        this.afterSchoolCareService = afterSchoolCareService;
        this.userService = userService;
    }

    @Override
    public List<Attendance> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Attendance findOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("attendance id not found: " + id));
    }

    @Override
    public Attendance save(Attendance newAttendance) {
        repository.save(newAttendance);

        return newAttendance;
    }

    @Override
    public Attendance saveByInputDTO(AttendanceInputDTO attendanceInputDTO, AfterSchoolCare afterSchoolCare) {
        Attendance attendance = getAttendanceByInputDTO(attendanceInputDTO);
        attendance.setAfterSchoolCare(afterSchoolCare);
        save(attendance);
        afterSchoolCare.addAttendance(attendance);
        afterSchoolCareService.save(afterSchoolCare);

        return attendance;
    }

    @NotNull
    @Override
    public Attendance getAttendanceByInputDTO(AttendanceInputDTO attendanceInputDTO) {
        Attendance attendance = new Attendance();

        attendance.setChild((User) userService.findByUsername(attendanceInputDTO.getChildUsername()));
        attendance.setNote(attendanceInputDTO.getNote());
        attendance.setChild((User) userService.findByUsername(attendanceInputDTO.getChildUsername()));
        attendance.setPredefinedLeaveTime(attendanceInputDTO.getPredefinedLeaveTime());
        attendance.setLatestArrivalTime(attendanceInputDTO.getLatestArrivalTime());
        attendance.setAllowedToLeaveAfterFinishedHomework(attendanceInputDTO.isAllowedToLeaveAfterFinishedHomework());
        return attendance;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public byte[] getAverageParticipantsList() throws Exception {
        File file = new File("H://CSV//averageParticipants.csv");
        if (!file.exists()) {
            boolean isCreated = file.createNewFile();
            if (!isCreated)
                throw new Exception();
        }

        List<AfterSchoolCareDTO> afterSchoolCaresMorning = afterSchoolCareService.getAll().stream()
                .filter(afterSchoolCare -> afterSchoolCare.getEndTime() != null && afterSchoolCare.getEndTime().toLocalTime().isAfter(LocalTime.of(6, 00, 00)))
                .filter(afterSchoolCare -> afterSchoolCare.getStartTime() != null && afterSchoolCare.getStartTime().toLocalTime().isBefore(LocalTime.NOON))
                .map(AfterSchoolCareConverter::toDto)
                .collect(Collectors.toList());

        List<AfterSchoolCareDTO> afterSchoolCaresAfternoon = afterSchoolCareService.getAll().stream()
                .filter(afterSchoolCare -> afterSchoolCare.getEndTime() != null && afterSchoolCare.getEndTime().toLocalTime().isAfter(LocalTime.NOON))
                .filter(afterSchoolCare -> afterSchoolCare.getStartTime() != null && afterSchoolCare.getStartTime().toLocalTime().isBefore(LocalTime.of(20, 00, 00)))
                .map(AfterSchoolCareConverter::toDto)
                .collect(Collectors.toList());

        try (FileWriter fileWriter = new FileWriter(file)) {
            String firstSectionHeader = "Betreuungsangebote in der Primarstufe und Betreuungs- und Ganztagsangebote im Rahmen der Offenen Ganztagsschule\n";
            String columnHeaders = " ; ;Montag;Dienstag;Mittwoch;Donnerstag;Freitag;Summe\n";
            String secondLineWeekdays = "Art der Angebote (Angabe im Antrag freiwillig, Im Verwendungsnachweis pflichtig) | Teilnehmer (TN)";
            String secondLine = "Zeit der Angebote von - bis (z.B. 7.00 -8.00 Uhr); " +
                    "Umfang in Zeitstunden (0,5 oder 1,0);" +
                    secondLineWeekdays + ";" +
                    secondLineWeekdays + ";" +
                    secondLineWeekdays + ";" +
                    secondLineWeekdays + ";" +
                    secondLineWeekdays + "\n";
            String secondSectionHeader = "Betreuungs- und Ganztagsangebote sind in der Sekundarstufe I nur ergänzend zu den Unterrichtszeiten und in der Primarstufe" +
                    "nur vor und nach den Verlässlichen Schulzeiten von vier (Klassen 1+2) und fünf (Klassen 3+4) Zeitstunden förderfähig\n";
            //Vormittags
            fileWriter.write(firstSectionHeader);
            fileWriter.write(columnHeaders);
            fileWriter.write(secondLine);
            for (AfterSchoolCareDTO afterSchoolCare : afterSchoolCaresMorning) {
                for (LocalTime time = LocalTime.of(6, 00, 00); time.isBefore(LocalTime.of(12, 00, 00)); time = time.plusMinutes(30)) {
                    fileWriter.write(afterSchoolCare.getStartTime().toLocalTime().toString() + ";"
                            + "0,5 ;"
                            + afterSchoolCare.getName() + " | " + afterSchoolCare.getAttendances().size() + "\n");
                }
            }
        /*

                    String monday = "";
                    int mondayParticipants = 0;
                    String tuesday = "";
                    int tuesdayParticipants = 0;
                    String wednesday = "";
                    int wednesdayParticipants = 0;
                    String thursday = "";
                    int thursdayParticipants = 0;
                    String friday = "";
                    int fridayParticipants = 0;

                    switch (afterSchoolCare.getStartTime().getDayOfWeek()) {
                        case MONDAY:
                            monday = afterSchoolCare.getName();
                            mondayParticipants = afterSchoolCare.getAttendances().size();
                            break;
                        case TUESDAY:
                            tuesday = afterSchoolCare.getName();
                            tuesdayParticipants = afterSchoolCare.getAttendances().size();
                            break;
                        case WEDNESDAY:
                            wednesday = afterSchoolCare.getName();
                            wednesdayParticipants = afterSchoolCare.getAttendances().size();
                            break;
                        case THURSDAY:
                            thursday = afterSchoolCare.getName();
                            thursdayParticipants = afterSchoolCare.getAttendances().size();
                            break;
                        case FRIDAY:
                            friday = afterSchoolCare.getName();
                            fridayParticipants = afterSchoolCare.getAttendances().size();
                            break;
                    }
*/

            //Nachmittags
            fileWriter.write(secondSectionHeader);
            for (AfterSchoolCareDTO afterSchoolCare : afterSchoolCaresAfternoon) {
                fileWriter.write(afterSchoolCare.getStartTime().toLocalTime().toString() + ";"
                        + "0,5 ;"
                        + afterSchoolCare.getName() + " | " + afterSchoolCare.getAttendances().size() + "\n");
            }

            byte[] bytes = FileUtil.readAsByteArray(file);
            fileWriter.close();
            //file.delete();
            return bytes;
        } catch (Exception e) {
            LOGGER.warn(e);
        }
        return new byte[0];
    }

    @Override
    public Attendance saveDto(AttendanceDTO dto) {
        //TODO Missing converter
        return null;
    }
}
