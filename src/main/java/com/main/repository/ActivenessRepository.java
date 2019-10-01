package com.main.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.model.Activeness;

@Repository
public interface ActivenessRepository extends CrudRepository<Activeness, Long>{
	
}
