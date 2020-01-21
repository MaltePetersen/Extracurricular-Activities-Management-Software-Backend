package com.main.dto.converters;

import com.main.dto.AttendanceDTO;
import com.main.dto.SimpleUserDTO;
import com.main.model.Attendance;
import com.main.model.User;

public class AttendanceConverter {

    public static AttendanceDTO toDto(Attendance attendance) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();

        attendanceDTO.setId(attendance.getId());

        attendanceDTO.setId(attendance.getId());
        if (attendance.getAfterSchoolCare()!= null) {
            attendanceDTO.setAfterSchoolCare(attendance.getAfterSchoolCare().getId());
        }

        //setChild
        if (attendance.getChild() != null) {
            User.UserBuilder builder = User.UserBuilder.next();
            builder.withUser(attendance.getChild());
            SimpleUserDTO userDTO = builder.toSimpleDto("CHILD");
            attendanceDTO.setChild(userDTO);
        }

        attendanceDTO.setArrivalTime(attendance.getArrivalTime());

        attendanceDTO.setLeaveTime(attendance.getLeaveTime());

        attendanceDTO.setNote(attendance.getNote());

        attendanceDTO.setStatus(attendance.determineStatus());

        return attendanceDTO;
    }
}