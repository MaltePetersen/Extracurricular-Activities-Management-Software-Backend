package com.main.repository;

import org.springframework.data.repository.CrudRepository;

import com.main.model.VerificationToken;
import com.main.model.interfaces.IVerificationToken;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken,Long> {
    IVerificationToken findByToken(String token);
        
    IVerificationToken findByUser_Email(String email);
}
