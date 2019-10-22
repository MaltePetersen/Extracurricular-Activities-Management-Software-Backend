package com.main.dto.converters;

import com.main.dto.AfterSchoolCareDTO;
import com.main.model.AfterSchoolCare;
import com.main.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class AfterSchoolCareConverter {

    private UserService userService;
    private SchoolConverter schoolConverter;

    public AfterSchoolCareConverter(UserService userService, SchoolConverter schoolConverter) {
        this.userService = userService;
        this.schoolConverter = schoolConverter;
    }

    public AfterSchoolCare fromDto(AfterSchoolCareDTO afterSchoolCareDTO) {
        AfterSchoolCare afterSchoolCare = new AfterSchoolCare();

        afterSchoolCare.setId(afterSchoolCareDTO.getId());
        afterSchoolCare.setStartTime(afterSchoolCareDTO.getStartTime());
        afterSchoolCare.setEndTime(afterSchoolCareDTO.getEndTime());
        afterSchoolCare.setParticipatingSchool(schoolConverter.fromDTO(afterSchoolCareDTO.getParticipatingSchool()));
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
        afterSchoolCareDTO.setParticipatingSchool(schoolConverter.toDto(afterSchoolCare.getParticipatingSchool()));
        if (afterSchoolCare.getEmployee() != null) {
            afterSchoolCareDTO.setEmployee(afterSchoolCare.getEmployee().getId());
        }

        return afterSchoolCareDTO;
    }
}
