package com.main.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.main.dto.converters.StringToLocalDatetimeConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceDTO {

    private Long id;

    private Long afterSchoolCare;

    private SimpleUserDTO child;

    @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
    private LocalDateTime arrivalTime;

    @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
    private LocalDateTime latestArrivalTime;

    @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
    private LocalDateTime leaveTime;

    @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
    private LocalDateTime predefinedLeaveTime;

    private boolean allowedToLeaveAfterFinishedHomework;

    private String note;

    private int status;
}
