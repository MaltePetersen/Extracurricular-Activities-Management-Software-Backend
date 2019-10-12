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
import java.util.List;
import java.util.Locale;
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

    private ResponseEntity<String> saveUser(@Valid Employee employee) {
        userRepository.save(employee);
        String token = UUID.randomUUID().toString();
        verificationTokenRepository.save(new VerificationToken(token, employee));
        String message = "localhost:8080/regitrationConfirm.html?token=" + token;
        emailService.sendSimpleMessage(employee.getEmail(),"Verification",message );
        return new ResponseEntity<>("Created Employee", HttpStatus.CREATED);
    }

    private ResponseEntity<String> saveUser(@Valid Parent parent) {
        userRepository.save(parent);
        String token = UUID.randomUUID().toString();
        verificationTokenRepository.save(new VerificationToken(token, parent));
        String message = "localhost:8080/regitrationConfirm.html?token=" + token;
        emailService.sendSimpleMessage(parent.getEmail(),"Verification",message );
        return new ResponseEntity<>("Created Parent", HttpStatus.CREATED);
    }

    private ResponseEntity<String> saveUser(@Valid Teacher teacher) {
        userRepository.save(teacher);
        String token = UUID.randomUUID().toString();
        verificationTokenRepository.save(new VerificationToken(token, teacher));
        String message = "localhost:8080/regitrationConfirm.html?token=" + token;
        emailService.sendSimpleMessage(teacher.getEmail(),"Verification",message );
        return new ResponseEntity<>("Created Teacher", HttpStatus.CREATED);
    }


    private ResponseEntity<String> saveUser(@Valid Child child) {
        userRepository.save(child);
        return new ResponseEntity<>("Created child", HttpStatus.CREATED);
    }

    private ResponseEntity<String> saveUser(@Valid User user) {
        userRepository.save(user);
        return new ResponseEntity<>("Created User", HttpStatus.CREATED);
    }

    private ResponseEntity<String> saveUser(@Valid SchoolCoordinator schoolCoordinator) {
        userRepository.save(schoolCoordinator);
        return new ResponseEntity<>("Created SchoolCoordinator", HttpStatus.CREATED);
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
                    return saveUser(new Employee(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getSubject(), userDTO.getIban(), userDTO.getAddress()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "MANAGEMENT":
                if (roles.contains("ROLE_MANAGEMENT"))
                    return saveUser(new Mangement(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "PARENT":
                return saveUser(new Parent(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber()));
            case "SCHOOLCOORDINATOR":
                if (roles.contains("ROLE_MANAGEMENT"))
                    return saveUser(new SchoolCoordinator(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "TEACHER":
                if (roles.contains("ROLE_MANAGEMENT") || roles.contains("ROLE_PARENT") || roles.contains("ROLE_SCHOOLCOORDINATOR"))
                    return saveUser(new Teacher(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);

            case "CHILD":
                if (roles.contains("ROLE_MANAGEMENT") || roles.contains("ROLE_PARENT") || roles.contains("ROLE_SCHOOLCOORDINATOR") || roles.contains("ROLE_TEACHER"))
                    return saveUser(new Child(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getSchoolClass()));
                return new ResponseEntity<>("You are lacking permissions to create this user Type", HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>("Role not valid", HttpStatus.BAD_REQUEST);
        }
    }
}
