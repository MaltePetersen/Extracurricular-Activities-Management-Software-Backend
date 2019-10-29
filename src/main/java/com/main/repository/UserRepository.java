package com.main.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

	User findByEmail(String email);

	List<User> findAllByVerified(boolean verified);

}