package com.main.methods;

import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.afterSchoolCare.AfternoonCare;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class AttendanceTest {

    /*@Test
    public void determineStatus0() {

        AfterSchoolCare afterSchoolCare = new AfternoonCare();
//        afterSchoolCare.setStartTime(null);
//        afterSchoolCare.setEndTime(null);

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        attendance.setArrivalTime(null);
        attendance.setLeaveTime(null);

        assertEquals(Attendance.AttendanceStatus.ERROR, attendance.determineStatus());
    }*/

    @Test
    public void determineStatus1() {

        AfterSchoolCare afterSchoolCare = new AfternoonCare();
        afterSchoolCare.setStartTime(LocalDateTime.now().plusHours(2));
        afterSchoolCare.setEndTime(LocalDateTime.now().plusDays(1));

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        attendance.setArrivalTime(null);
        attendance.setLeaveTime(null);

        assertEquals(Attendance.AttendanceStatus.REGISTERED, attendance.determineStatus());

    }

    @Test
    public void determineStatus1LatestArrivalTime() {

        AfterSchoolCare afterSchoolCare = new AfternoonCare();
        afterSchoolCare.setStartTime(LocalDateTime.now().minusMinutes(30));
        afterSchoolCare.setEndTime(LocalDateTime.now().plusDays(1));

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        attendance.setArrivalTime(null);
        attendance.setLeaveTime(null);
        attendance.setLatestArrivalTime(LocalDateTime.now().plusMinutes(10));

        assertEquals(Attendance.AttendanceStatus.REGISTERED, attendance.determineStatus());

    }

    @Test
    public void determineStatus2() {

        AfterSchoolCare afterSchoolCare = new AfternoonCare();
        afterSchoolCare.setStartTime(LocalDateTime.now().minusHours(2));
        afterSchoolCare.setEndTime(LocalDateTime.now().plusDays(1));

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        attendance.setArrivalTime(null);
        attendance.setLeaveTime(null);

        assertEquals(Attendance.AttendanceStatus.OVERDUE, attendance.determineStatus());

    }

    @Test
    public void determineStatus2LatestArrivalTime() {

        AfterSchoolCare afterSchoolCare = new AfternoonCare();
        afterSchoolCare.setStartTime(LocalDateTime.now().minusHours(2));
        afterSchoolCare.setEndTime(LocalDateTime.now().plusDays(1));

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        attendance.setArrivalTime(null);
        attendance.setLeaveTime(null);
        attendance.setLatestArrivalTime(LocalDateTime.now().minusHours(1));

        assertEquals(Attendance.AttendanceStatus.OVERDUE, attendance.determineStatus());

    }

    @Test
    public void determineStatus3() {

        AfterSchoolCare afterSchoolCare = new AfternoonCare();
        afterSchoolCare.setStartTime(LocalDateTime.now().minusHours(2));
        afterSchoolCare.setEndTime(LocalDateTime.now().plusDays(1));

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        attendance.setArrivalTime(LocalDateTime.now());
        attendance.setLeaveTime(null);

        assertEquals(Attendance.AttendanceStatus.PRESENT, attendance.determineStatus());

    }

    @Test
    public void determineStatus4() {

        AfterSchoolCare afterSchoolCare = new AfternoonCare();
        afterSchoolCare.setStartTime(LocalDateTime.now().minusHours(2));
        afterSchoolCare.setEndTime(LocalDateTime.now().plusDays(1));

        Attendance attendance = new Attendance();
        attendance.setAfterSchoolCare(afterSchoolCare);
        attendance.setArrivalTime(LocalDateTime.now());
        attendance.setLeaveTime(LocalDateTime.now().plusHours(6));

        assertEquals(Attendance.AttendanceStatus.GONE, attendance.determineStatus());

    }

}
