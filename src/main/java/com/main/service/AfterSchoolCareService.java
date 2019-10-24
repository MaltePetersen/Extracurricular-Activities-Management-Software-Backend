package com.main.service;

import com.main.model.AfterSchoolCare;
import com.main.model.Attendance;

import java.util.List;

public interface AfterSchoolCareService {
	List<AfterSchoolCare> getAll();

    AfterSchoolCare findOne(Long id);

    AfterSchoolCare save(AfterSchoolCare newAfterSchoolCare);

    AfterSchoolCare addAttendance(Long id, Attendance attendance);

    void deleteById(Long id);
}
