package com.main.service.implementations;

import com.main.dto.AfterSchoolCareDTO;
import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfterSchoolCare;

import java.util.List;

public interface AfterSchoolCareService {
	List<AfterSchoolCare> getAll();

    List<AfterSchoolCare> getAllByParent(String username);

    AfterSchoolCare findOne(Long id);

    AfterSchoolCare save(AfterSchoolCare newAfterSchoolCare);

    AfterSchoolCare addAttendance(Long id, Attendance attendance);

    void deleteById(Long id);

    List<AfterSchoolCareDTO> findAllByTypeWorkingGroup();

    AfterSchoolCareDTO createNew(AfterSchoolCareDTO afterSchoolCareDTO);

    void update(AfterSchoolCare afterSchoolCare, AfterSchoolCareDTO afterSchoolCareDTO);

    byte[] getAttendanceList() throws Exception;
}
