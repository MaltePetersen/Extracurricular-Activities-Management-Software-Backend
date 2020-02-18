package com.main.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.main.dto.converters.StringToLocalDatetimeConverter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AfterSchoolCareDTO {
	private Long id;

	private int type;

	private boolean closed;

	private String name;

	@JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
	private LocalDateTime startTime;

	@JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
	private LocalDateTime endTime;

	private Long participatingSchool;

	private SimpleUserDTO owner;

	private List<AttendanceDTO> attendances;
}
