package com.main.repository;

import com.main.model.AfterSchoolCare;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterSchoolCareRepository extends CrudRepository<AfterSchoolCare, Long> {

}