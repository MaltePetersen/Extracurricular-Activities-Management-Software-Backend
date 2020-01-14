package com.main.dto;

import com.main.dto.interfaces.IUserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceDTO {

    private Long id;

    private Long afterSchoolCare;

    private IUserDTO child;

    private LocalDateTime arrivalTime;

    //private LocalDateTime latestArrivalTime;

    private LocalDateTime leaveTime;

    //private LocalDateTime predefinedLeaveTime;

    private String note;

    private int status;

}
