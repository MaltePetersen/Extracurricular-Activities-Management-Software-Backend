package com.main.service;

import com.main.model.AfterSchoolCare;
import com.main.repository.AfterSchoolCareRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AfterSchoolCareServiceImpl implements AfterSchoolCareService {

	private AfterSchoolCareRepository repository;

    AfterSchoolCareServiceImpl(AfterSchoolCareRepository repository) {
        this.repository = repository;
    }

    @Override
	public List<AfterSchoolCare> getAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
	}

    @Override
    public AfterSchoolCare findOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("after school care id not found: " + id));
    }

    @Override
	public AfterSchoolCare save(AfterSchoolCare newAfterSchoolCare) {
    	repository.save(newAfterSchoolCare);

		return newAfterSchoolCare;
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
