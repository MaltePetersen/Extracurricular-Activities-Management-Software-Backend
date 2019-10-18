package com.main.service;

import com.main.model.School;
import com.main.model.interfaces.ISchool;
import com.main.repository.SchoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SchoolServiceImpl implements SchoolService {

	private SchoolRepository repository;

    SchoolServiceImpl(SchoolRepository repository) {
        this.repository = repository;
    }

    @Override
	public List<School> getAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
	}

    @Override
    public School findOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("school id not found: " + id));
    }

    @Override
	public ISchool save(School newSchool) {
		repository.save(newSchool);

		return newSchool;
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
