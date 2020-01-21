package com.main.dto;

import com.main.dto.interfaces.IUserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AfterSchoolCareDTO {
	private Long id;

	private int type;

	private String name;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private Long participatingSchool;

	private IUserDTO owner;

	private List<AttendanceDTO> attendances;
}
