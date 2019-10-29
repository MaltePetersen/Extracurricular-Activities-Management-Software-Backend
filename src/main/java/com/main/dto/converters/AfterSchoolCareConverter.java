package com.main.dto.converters;

import com.main.dto.AfterSchoolCareDTO;
import com.main.model.AfterSchoolCare;
import com.main.model.Attendance;
import com.main.service.UserService;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

public class AfterSchoolCareConverter {

	public static AfterSchoolCareDTO toDto(AfterSchoolCare afterSchoolCare) {
		AfterSchoolCareDTO afterSchoolCareDTO = new AfterSchoolCareDTO();

		afterSchoolCareDTO.setId(afterSchoolCare.getId());
		afterSchoolCareDTO.setStartTime(afterSchoolCare.getStartTime());
		afterSchoolCareDTO.setEndTime(afterSchoolCare.getEndTime());
		if (afterSchoolCare.getParticipatingSchool() != null) {
			afterSchoolCareDTO.setParticipatingSchool(SchoolConverter.toDto(afterSchoolCare.getParticipatingSchool()));
		}
		if (afterSchoolCare.getEmployee() != null) {
			afterSchoolCareDTO.setEmployee(afterSchoolCare.getEmployee().getId());
		}

		if (afterSchoolCare.getAttendances() != null) {
			afterSchoolCareDTO.setAttendances(
					afterSchoolCare.getAttendances().stream().map(Attendance::getId).collect(Collectors.toList()));
		}

		return afterSchoolCareDTO;
	}
}
