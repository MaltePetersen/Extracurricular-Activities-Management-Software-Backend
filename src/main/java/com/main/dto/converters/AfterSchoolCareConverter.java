package com.main.dto.converters;

import com.main.dto.AfterSchoolCareDTO;
import com.main.model.AfterSchoolCare;
import com.main.service.SchoolService;
import com.main.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class AfterSchoolCareConverter {

    private UserService userService;
    private SchoolService schoolService;

    public AfterSchoolCareConverter(UserService userService, SchoolService schoolService) {
        this.userService = userService;
        this.schoolService = schoolService;
    }

    public AfterSchoolCare fromDto(AfterSchoolCareDTO afterSchoolCareDTO) {
        AfterSchoolCare afterSchoolCare = new AfterSchoolCare();

        afterSchoolCare.setId(afterSchoolCareDTO.getId());
        afterSchoolCare.setStartTime(afterSchoolCareDTO.getStartTime());
        afterSchoolCare.setEndTime(afterSchoolCareDTO.getEndTime());
        if (afterSchoolCareDTO.getParticipatingSchool() != null) {
            afterSchoolCare.setParticipatingSchool(schoolService.findOne(afterSchoolCareDTO.getParticipatingSchool()));
        }
        if (afterSchoolCareDTO.getEmployee() != null) {
            afterSchoolCare.setEmployee(userService.findOne(afterSchoolCareDTO.getEmployee()));
        }

        return afterSchoolCare;
    }

    public AfterSchoolCareDTO toDto(AfterSchoolCare afterSchoolCare) {
        AfterSchoolCareDTO afterSchoolCareDTO = new AfterSchoolCareDTO();

        afterSchoolCareDTO.setId(afterSchoolCare.getId());
        afterSchoolCareDTO.setStartTime(afterSchoolCare.getStartTime());
        afterSchoolCareDTO.setEndTime(afterSchoolCare.getEndTime());
        if (afterSchoolCare.getParticipatingSchool() != null) {
            afterSchoolCareDTO.setParticipatingSchool(afterSchoolCare.getParticipatingSchool().getId());
        }
        if (afterSchoolCare.getEmployee() != null) {
            afterSchoolCareDTO.setEmployee(afterSchoolCare.getEmployee().getId());
        }

        return afterSchoolCareDTO;
    }
}
