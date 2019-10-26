package com.main.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.model.Privilege;
@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

	Privilege findByName(String name);

	List<Privilege> findByRoles_Name(String name);

}
