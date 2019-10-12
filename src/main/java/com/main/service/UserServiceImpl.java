package com.main.service;

import com.main.dto.UserDTO;
import com.main.model.VerificationToken;
import com.main.model.userTypes.*;
import com.main.repository.UserRepository;
import com.main.repository.VerificationTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository verificationTokenRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }
    private void creatingAndSendingVerficationToken(User user){
        String token = UUID.randomUUID().toString();
        verificationTokenRepository.save(new VerificationToken(token, user));
        String message = "localhost:8080/regitrationConfirm?token=" + token;
        emailService.sendSimpleMessage(user.getEmail(),"Verification",message );
    }

    private ResponseEntity<String> saveUser(@Valid User user) {
        userRepository.save(user);
        if(user.getEmail() != null && !user.getEmail().equals("")){
            creatingAndSendingVerficationToken(user);
        }
        String message = "Created "+ Arrays.toString(user.getAuthorities().toArray());
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> save(UserDTO userDTO, Authentication auth) {
        List<String> roles = new ArrayList<>();
        if (auth != null)
            auth.getAuthorities().forEach((a) -> {
                roles.add(a.getAuthority());
            });
        if (userRepository.findByUsername(userDTO.getUsername()) != null)
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        switch (userDTO.getUserType()) {
            case "USER":
                if (roles.contains("ROLE_MANAGEMENT") || roles.contains("ROLE_SCHOOLCOORDINATOR"))
                    return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);
            case "EMPLOYEE":
                if (roles.contains("ROLE_MANAGEMENT"))
                    return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getSubject(), userDTO.getIban(), userDTO.getAddress(),userDTO.isSchoolCoordinator()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "MANAGEMENT":
                if (roles.contains("ROLE_MANAGEMENT"))
                    return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "PARENT":
                return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber()));
            case "SCHOOLCOORDINATOR":
                if (roles.contains("ROLE_MANAGEMENT"))
                    return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "TEACHER":
                if (roles.contains("ROLE_MANAGEMENT") || roles.contains("ROLE_PARENT") || roles.contains("ROLE_SCHOOLCOORDINATOR"))
                    return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber(),userDTO.getSubject(),userDTO.isSchoolCoordinator()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "CHILD":
                if (roles.contains("ROLE_MANAGEMENT") || roles.contains("ROLE_PARENT") || roles.contains("ROLE_SCHOOLCOORDINATOR") || roles.contains("ROLE_TEACHER"))
                    return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getSchoolClass()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>("Role not valid", HttpStatus.BAD_REQUEST);
        }
    }
}
