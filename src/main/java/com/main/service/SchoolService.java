package com.main.service;

import com.main.model.School;
import com.main.model.interfaces.ISchool;

import java.util.List;

public interface SchoolService {
	List<School> getAll();

	School findOne(Long id);

    ISchool save(School newSchool);

    void deleteById(Long id);
}
