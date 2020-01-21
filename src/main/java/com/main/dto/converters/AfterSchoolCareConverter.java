package com.main.dto.converters;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfterSchoolCare;

import java.util.stream.Collectors;

public class AfterSchoolCareConverter {

	public static AfterSchoolCareDTO toDto(AfterSchoolCare afterSchoolCare) {
		AfterSchoolCareDTO afterSchoolCareDTO = new AfterSchoolCareDTO();

		afterSchoolCareDTO.setId(afterSchoolCare.getId());
		afterSchoolCareDTO.setType(afterSchoolCare.getType().getId());
		afterSchoolCareDTO.setName(afterSchoolCare.getName());
		afterSchoolCareDTO.setStartTime(afterSchoolCare.getStartTime());
		afterSchoolCareDTO.setEndTime(afterSchoolCare.getEndTime());


		if (afterSchoolCare.getParticipatingSchool() != null) {
			afterSchoolCareDTO.setParticipatingSchool(afterSchoolCare.getParticipatingSchool().getId());
		}

		if (afterSchoolCare.getOwner() != null) {
			User.UserBuilder builder = User.UserBuilder.next();
			builder.withUser(afterSchoolCare.getOwner());
			IUserDTO userDTO = builder.toDto("EMPLOYEE");
			afterSchoolCareDTO.setOwner(userDTO);
		}

		if (afterSchoolCare.getAttendances() != null) {
			afterSchoolCareDTO.setAttendances(
					afterSchoolCare.getAttendances().stream().map(AttendanceConverter::toDto).collect(Collectors.toList()));
		}

		return afterSchoolCareDTO;
	}
}
