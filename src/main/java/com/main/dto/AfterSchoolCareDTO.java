package com.main.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AfterSchoolCareDTO {

	private Long id;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private SchoolDTO participatingSchool;

	private Long employee;
}
