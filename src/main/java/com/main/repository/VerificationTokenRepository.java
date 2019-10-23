package com.main.repository;

import com.main.model.VerificationToken;
import com.main.model.interfaces.IVerificationToken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken,Long> {
    IVerificationToken findByToken(String token);
        
    IVerificationToken findByUser_Email(String email);
}
