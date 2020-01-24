package com.main.service;

import com.main.dto.AttendanceInputDTO;
import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private AttendanceRepository repository;
    private AfterSchoolCareService afterSchoolCareService;
    private UserService userService;

    public AttendanceServiceImpl(AttendanceRepository repository, AfterSchoolCareService afterSchoolCareService, UserService userService){
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
        Attendance attendance = new Attendance();

        attendance.setChild(userService.findOne(attendanceInputDTO.getChildId()));
        attendance.setNote(attendanceInputDTO.getNote());
        attendance.setAfterSchoolCare(afterSchoolCare);

        save(attendance);

        afterSchoolCare.addAttendance(attendance);
        afterSchoolCareService.save(afterSchoolCare);

        return attendance;
    }

    @Override
    public void deleteById(Long id)  {
        repository.deleteById(id);
    }
}
