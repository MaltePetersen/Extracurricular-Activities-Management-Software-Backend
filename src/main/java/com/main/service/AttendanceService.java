package com.main.service;

import com.main.model.Attendance;

import java.util.List;

public interface AttendanceService {

    List<Attendance> getAll();

    Attendance findOne(Long id);

    Attendance save(Attendance newAttendance);

    void deleteById(Long id);

}
