package com.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.model.Member;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);
	

	List<Member> findByUsernameStartingWithIgnoreCase(String username);

}