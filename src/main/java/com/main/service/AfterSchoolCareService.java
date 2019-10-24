package com.main.service;

import com.main.model.AfterSchoolCare;

import java.util.List;

public interface AfterSchoolCareService {
	List<AfterSchoolCare> getAll();

    AfterSchoolCare findOne(Long id);

    AfterSchoolCare save(AfterSchoolCare newAfterSchoolCare);

    void deleteById(Long id);
}
