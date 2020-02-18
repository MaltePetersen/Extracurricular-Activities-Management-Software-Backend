package com.main.service.interfaces;

import com.main.dto.AttendanceDTO;
import com.main.dto.AttendanceInputDTO;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
  public byte[] getAttendanceList() throws Exception {
    List<Attendance> all = repository.findByIsClosedTrue();
    File file = new File("attendance.csv");
    if (!file.exists()) {
      boolean isCreated = file.createNewFile();
      if(!isCreated)
        throw new Exception();
    }
    FileWriter fileWriter = new FileWriter(file);

    for (Attendance attendance : all) {
      String name = attendance.getChild().getFullname();
      LocalDateTime arrival = attendance.getArrivalTime();
      LocalDateTime leaveTime = attendance.getLeaveTime();
      String type = attendance.getAfterSchoolCare().getType().name();
      String line = name + ";" + arrival + ";" + leaveTime + ";" + type;
      fileWriter.write(line);
    }

    byte[] bytes = FileUtil.readAsByteArray(file);
    fileWriter.close();
    file.delete();
    return bytes;
  }

    @Override
    @Transactional
    public byte[] getAverageParticipantsList() throws Exception {
        File file = new File("H://CSV//averageParticipants.csv");
        if(!file.exists()) {
            boolean isCreated = file.createNewFile();
            if (!isCreated)
                throw new Exception();
        }

        FileWriter fileWriter = new FileWriter(file);
        String firstLine = " ; ;Montag;Dienstag;Mittwoch;Donnerstag;Freitag;Summe";
        String secondLineWeekdays = "Art der Angebote (Angabe im Antrag freiwillig, Im Verwendungsnachweis pflichtig) | Teilnehmer (TN)";
        String secondLine = "\nZeit der Angebote von - bis (z.B. 7.00 -8.00 Uhr); " +
                            "Umfang in Zeitstunden (0,5 oder 1,0);" +
                            secondLineWeekdays + ";" +
                            secondLineWeekdays + ";" +
                            secondLineWeekdays + ";" +
                            secondLineWeekdays + ";" +
                            secondLineWeekdays + "\n";
        fileWriter.write(firstLine);
        fileWriter.write(secondLine);

    byte[] bytes = FileUtil.readAsByteArray(file);
    System.out.println(firstLine);
    fileWriter.close();
    //file.delete();
    return bytes;
  }

  @Override
  public Attendance saveDto(AttendanceDTO dto) {
    //TODO Missing converter
    return null;
  }
}
