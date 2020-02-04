package com.main.repository;

import com.main.model.Attendance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends CrudRepository<Attendance, Long> {
    List<Attendance> findByIsClosedTrue();
}