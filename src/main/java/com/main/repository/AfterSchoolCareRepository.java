package com.main.repository;

import com.main.model.afterSchoolCare.AfterSchoolCare;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterSchoolCareRepository extends CrudRepository<AfterSchoolCare, Long> {
    List<AfterSchoolCare> findByClosedTrue();
}