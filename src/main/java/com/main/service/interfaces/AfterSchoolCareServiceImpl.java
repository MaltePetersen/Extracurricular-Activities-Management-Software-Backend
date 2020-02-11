package com.main.service.interfaces;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.AttendanceDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.afterSchoolCare.AfterSchoolCareCreator;
import com.main.repository.AfterSchoolCareRepository;
import com.main.repository.AttendanceRepository;
import com.main.service.implementations.AfterSchoolCareService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public AfterSchoolCareDTO createNew(AfterSchoolCareDTO afterSchoolCareDTO) {
        AfterSchoolCare afterSchoolCare = null;

        switch (afterSchoolCareDTO.getType()) {
            case 1:
                AfterSchoolCareCreator.createAfternoonCare();
            case 2:
                AfterSchoolCareCreator.createWorkingGroup();
            default:
                AfterSchoolCareCreator.createAmplification();
        }

        afterSchoolCare.setStartTime(afterSchoolCareDTO.getStartTime());
        afterSchoolCare.setEndTime(afterSchoolCareDTO.getEndTime());
        afterSchoolCare.setName(afterSchoolCareDTO.getName());

        return AfterSchoolCareConverter.toDto(save(afterSchoolCare));
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
                if(optionalAttendance.isPresent()){
                    Attendance attendance = optionalAttendance.get();
                    afterSchoolCare.addAttendance(attendance);
                }
            }
        }
        //TODO Adding User change
    }
}
