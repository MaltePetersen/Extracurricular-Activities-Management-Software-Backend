package com.main.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.main.dto.converters.StringToLocalDatetimeConverter;
import com.main.dto.interfaces.IUserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AfterSchoolCareDTO {
	private Long id;

	private int type;

	private String name;

	@JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
	private LocalDateTime startTime;

	@JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
	private LocalDateTime endTime;

	private Long participatingSchool;

	private SimpleUserDTO employee;

	private List<AttendanceDTO> attendances;
}
