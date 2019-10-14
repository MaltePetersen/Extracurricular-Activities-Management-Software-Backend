package com.main.service;

import com.main.model.School;

import java.util.List;

public interface SchoolService {
	List<School> getAll();

	School findOne(Long id);

    School save(School newSchool);

    void deleteById(Long id);
}
