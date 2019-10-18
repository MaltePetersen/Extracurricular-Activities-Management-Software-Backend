package com.main.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SchoolDTO {

	private Long id;

	@NotBlank(message = "school name is mandatory")
	private String name;

	@NotBlank(message = "school address is mandatory")
	private String address;

	private String email;
	private String phoneNumber;
}
