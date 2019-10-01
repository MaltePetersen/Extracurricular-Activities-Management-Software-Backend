package com.main.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.model.Activeness;
import com.main.repository.ActivenessRepository;

@Service
public class ActivenessServiceImpl implements ActivenessService {

	@Autowired
	private ActivenessRepository repository;

	@Override
	public List<Activeness> getAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
	}

}
