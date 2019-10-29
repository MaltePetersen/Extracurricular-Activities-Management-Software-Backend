package com.main.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AfterSchoolCareDTO {

	private Long id;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private Long participatingSchool;

	private Long employee;
}
