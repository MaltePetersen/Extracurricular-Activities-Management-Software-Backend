package com.main.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttendanceInputDTO {
    @NotBlank(message = "Child Id is mandatory")
    private Long childId;

    private String note;
}
