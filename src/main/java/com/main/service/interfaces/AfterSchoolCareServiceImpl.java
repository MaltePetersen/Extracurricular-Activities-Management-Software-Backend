package com.main.service.interfaces;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.AttendanceDTO;
import com.main.dto.WorkingGroupDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.afterSchoolCare.AfterSchoolCareCreator;
import com.main.model.afterSchoolCare.WorkingGroup;
import com.main.repository.AfterSchoolCareRepository;
import com.main.repository.AttendanceRepository;
import com.main.service.implementations.AfterSchoolCareService;
import org.aspectj.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Service
public class AfterSchoolCareServiceImpl implements AfterSchoolCareService {

    private AfterSchoolCareRepository repository;

    private AttendanceRepository attendanceRepository;

    public AfterSchoolCareServiceImpl(AfterSchoolCareRepository repository, AttendanceRepository attendanceRepository) {
        this.repository = repository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public List<AfterSchoolCare> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<AfterSchoolCare> getAllByParent(String username) {
        return StreamSupport.stream(repository.findAll().spliterator(), false).
                filter(afterSchoolCare -> afterSchoolCare.getAttendances().stream().anyMatch(attendance -> attendance.getChild() != null && attendance.getChild().getParent() != null && attendance.getChild().getParent().getUsername().equals(username)))
                .collect(Collectors.toList());
    }

    @Override
    public AfterSchoolCare findOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("after school care id not found: " + id));
    }

    @Override
    public AfterSchoolCare save(AfterSchoolCare newAfterSchoolCare) {
        repository.save(newAfterSchoolCare);
        return newAfterSchoolCare;
    }

    /***
     *
     * Fügt einem nachmittagsbetreuungs-Objekt eine Anwesenheit hinzu und speichert es.
     *
     * @param id Long
     * @param attendance Attendance
     * @return gespeichertes Nachmittagsbetreuungs-Objekt
     */
    @Override
    public AfterSchoolCare addAttendance(Long id, Attendance attendance) {

        AfterSchoolCare afterSchoolCare = this.findOne(id);

        attendance.setAfterSchoolCare(afterSchoolCare);

        attendanceRepository.save(attendance);

        afterSchoolCare.addAttendance(attendance);

        return this.save(afterSchoolCare);
    }


    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<AfterSchoolCareDTO> findAllByTypeWorkingGroup() {
        List<AfterSchoolCare> all = StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .filter(each -> each.getType().equals(AfterSchoolCare.Type.WORKING_GROUP))
                .collect(Collectors.toList());

        return all.stream().map(AfterSchoolCareConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public AfterSchoolCareDTO createNew(@NotNull AfterSchoolCareDTO afterSchoolCareDTO) {
        AfterSchoolCare afterSchoolCare;

        switch (afterSchoolCareDTO.getType()) {
            case 1:
                afterSchoolCare = AfterSchoolCareCreator.createAfternoonCare();
                break;
            case 2:
                afterSchoolCare = AfterSchoolCareCreator.createWorkingGroup();
                ((WorkingGroup) afterSchoolCare).setExternal(((WorkingGroupDTO) afterSchoolCareDTO).isExternal());
                break;
            case 3:
                afterSchoolCare = AfterSchoolCareCreator.createRemidial();
                break;
            case 4:
                afterSchoolCare = AfterSchoolCareCreator.createAmplification();
                break;
            default:
                afterSchoolCare = null;
                break;
        }

        if (afterSchoolCare != null) {
            afterSchoolCare.setStartTime(afterSchoolCareDTO.getStartTime());
            afterSchoolCare.setEndTime(afterSchoolCareDTO.getEndTime());
            afterSchoolCare.setName(afterSchoolCareDTO.getName());

            return AfterSchoolCareConverter.toDto(save(afterSchoolCare));
        } else {
            return null;
        }
    }


    @Override
    @Transactional
    public void update(AfterSchoolCare afterSchoolCare, AfterSchoolCareDTO afterSchoolCareDTO) {
        if (afterSchoolCare.getName() != null)
            afterSchoolCare.setName(afterSchoolCareDTO.getName());
        if (afterSchoolCareDTO.getEndTime() != null)
            afterSchoolCare.setEndTime(afterSchoolCareDTO.getEndTime());
        if (afterSchoolCareDTO.getStartTime() != null)
            afterSchoolCare.setStartTime(afterSchoolCareDTO.getStartTime());
        if (afterSchoolCareDTO.getAttendances() != null) {
            for (AttendanceDTO attendanceDTO : afterSchoolCareDTO.getAttendances()) {
                Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceDTO.getId());
                if (optionalAttendance.isPresent()) {
                    Attendance attendance = optionalAttendance.get();
                    afterSchoolCare.addAttendance(attendance);
                }
            }
        }
        //TODO Adding User change
    }

    @Override
    @Transactional
    public byte[] getAttendanceList() throws Exception {
        List<Attendance> all = repository.findByClosedTrue().stream().map(AfterSchoolCare::getAttendances).collect(ArrayList::new, List::addAll, List::addAll);

        File file = new File("attendance.csv");
        if (!file.exists()) {
            boolean isCreated = file.createNewFile();
            if (!isCreated)
                throw new Exception();
        }
        try (FileWriter fileWriter = new FileWriter(file);
        ) {

            for (Attendance attendance : all) {
                String name = attendance.getChild().getFullname();
                LocalDateTime arrival = attendance.getArrivalTime();
                LocalDateTime leaveTime = attendance.getLeaveTime();
                String type = attendance.getAfterSchoolCare().getType().name();
                String line = name + ";" + arrival + ";" + leaveTime + ";" + type;
                fileWriter.write(line);
                fileWriter.flush();
            }
            byte[] bytes = FileUtil.readAsByteArray(file);

            return bytes;
        } catch (Exception e) {
            LOGGER.warn(e);
        }
        return new byte[0];
    }
}
