package com.main.dto.converters;

import org.springframework.stereotype.Component;

import com.main.dto.AfterSchoolCareDTO;
import com.main.model.AfterSchoolCare;
import com.main.model.Attendance;

import java.util.stream.Collectors;

public class AfterSchoolCareConverter {

	public static AfterSchoolCareDTO toDto(AfterSchoolCare afterSchoolCare) {
		AfterSchoolCareDTO afterSchoolCareDTO = new AfterSchoolCareDTO();

		afterSchoolCareDTO.setId(afterSchoolCare.getId());
		afterSchoolCareDTO.setStartTime(afterSchoolCare.getStartTime());
		afterSchoolCareDTO.setEndTime(afterSchoolCare.getEndTime());
		if (afterSchoolCare.getParticipatingSchool() != null) {
			afterSchoolCareDTO.setParticipatingSchool(afterSchoolCare.getParticipatingSchool().getId());
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
