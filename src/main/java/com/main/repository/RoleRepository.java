package com.main.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{

	Role findByName(String string);

	List<Role> findByUsers_Username(String username);

}
