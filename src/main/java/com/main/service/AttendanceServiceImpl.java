package com.main.service;

import com.main.model.Attendance;
import com.main.repository.AfterSchoolCareRepository;
import com.main.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private AttendanceRepository repository;

    public AttendanceServiceImpl(AttendanceRepository repository){
        this.repository = repository;

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
    public void deleteById(Long id)  {
        repository.deleteById(id);
    }
}
