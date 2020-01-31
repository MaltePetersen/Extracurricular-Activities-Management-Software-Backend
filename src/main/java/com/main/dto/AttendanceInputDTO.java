package com.main.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.main.dto.converters.StringToLocalDatetimeConverter;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class AttendanceInputDTO {
    @NotBlank(message = "Child Username is mandatory")
    private String childUsername;

    @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
    private LocalDateTime latestArrivalTime;

    @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
    private LocalDateTime predefinedLeaveTime;

    private boolean allowedToLeaveAfterFinishedHomework;

    private String note;

    private Long childId;
}
