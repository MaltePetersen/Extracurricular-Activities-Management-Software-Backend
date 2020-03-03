package com.main.service.implementations;

import com.main.dto.AttendanceDTO;
import com.main.dto.AttendanceInputDTO;
import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfterSchoolCare;

import java.io.FileNotFoundException;
import java.util.List;

public interface AttendanceService {

  List<Attendance> getAll();

  Attendance findOne(Long id);

  Attendance save(Attendance newAttendance);

  Attendance saveByInputDTO(AttendanceInputDTO attendanceInputDTO, AfterSchoolCare afterSchoolCare);

  void deleteById(Long id);

  Attendance getAttendanceByInputDTO(AttendanceInputDTO dto);

  Attendance saveDto(AttendanceDTO dto);


  byte[] getAverageParticipantsList() throws FileNotFoundException, Exception;
}