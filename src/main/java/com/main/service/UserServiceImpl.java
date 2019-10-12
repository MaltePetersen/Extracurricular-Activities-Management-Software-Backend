package com.main.service;

import com.main.dto.UserDTO;
import com.main.model.userTypes.*;
import com.main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> saveUser(@Valid User user) {
        userRepository.save(user);
        return new ResponseEntity<>("Created User", HttpStatus.CREATED);
    }

    public ResponseEntity<String> saveUser(@Valid Employee employee) {
        userRepository.save(employee);
        return new ResponseEntity<>("Created Employee", HttpStatus.CREATED);
    }

    public ResponseEntity<String> saveUser(@Valid Parent parent) {
        userRepository.save(parent);
        return new ResponseEntity<>("Created Parent", HttpStatus.CREATED);
    }

    public ResponseEntity<String> saveUser(@Valid SchoolCoordinator schoolCoordinator) {
        userRepository.save(schoolCoordinator);
        return new ResponseEntity<>("Created SchoolCoordinator", HttpStatus.CREATED);
    }

    public ResponseEntity<String> saveUser(@Valid Teacher teacher) {
        userRepository.save(teacher);
        return new ResponseEntity<>("Created Teacher", HttpStatus.CREATED);
    }

    public ResponseEntity<String> saveUser(@Valid Child child) {
        userRepository.save(child);
        return new ResponseEntity<>("Created child", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> save(UserDTO userDTO) {
        switch (userDTO.getUserType()) {
            case "USER":
                return saveUser(new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname()));
            case "EMPLOYEE":
                return saveUser(new Employee(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getSubject(), userDTO.getIban(), userDTO.getAddress()));
            case "MANAGEMENT":
                return saveUser(new Mangement(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress()));
            case "PARENT":
                return saveUser(new Parent(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber()));
            case "SCHOOLCOORDINATOR":
                return saveUser(new SchoolCoordinator(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname()));
            case "TEACHER":
                return saveUser(new Teacher(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getEmail(), userDTO.getPhoneNumber()));
            case "CHILD":
                return saveUser(new Child(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullname(), userDTO.getSchoolClass()));
            default:
                return new ResponseEntity<>("Role not valid", HttpStatus.BAD_REQUEST);
        }
    }
}
