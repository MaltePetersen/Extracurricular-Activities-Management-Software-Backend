package com.main.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.main.model.Attendance;

@Data
public class AfterSchoolCareDTO {

	private Long id;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private SchoolDTO participatingSchool;

	private List<Long> attendances;
	
	private Long employee;
}
