package com.main.dto.converters;

import com.main.dto.SchoolDTO;
import com.main.model.School;
import org.springframework.stereotype.Component;

public class SchoolConverter {

    public static School fromDTO(SchoolDTO schoolDTO) {
        School school = new School();

        school.setId(schoolDTO.getId());
        school.setName(schoolDTO.getName());
        school.setAddress(schoolDTO.getAddress());
        school.setEmail(schoolDTO.getEmail());
        school.setPhoneNumber(schoolDTO.getPhoneNumber());

        return school;
    }

    public static SchoolDTO toDto(School school) {
        SchoolDTO schoolDTO = new SchoolDTO();

        schoolDTO.setId(school.getId());
        schoolDTO.setName(school.getName());
        schoolDTO.setAddress(school.getAddress());
        schoolDTO.setEmail(school.getEmail());
        schoolDTO.setPhoneNumber(school.getPhoneNumber());

        return schoolDTO;
    }
}
