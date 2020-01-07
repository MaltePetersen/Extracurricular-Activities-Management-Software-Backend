package com.main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.main.dto.UserDTO;
import com.main.model.User;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;

@CrossOrigin
@RestController
public class ManagementController {


    private UserService userService;
    private UserDTOValidator userDTOValidator;

    public ManagementController(UserService userService, UserDTOValidator userDTOValidator) {
        this.userService = userService;
        this.userDTOValidator = userDTOValidator;
    }

    @GetMapping("/api/management/not_enabled_users")
    public List<UserDTO> allNotEnabledUser() {
        List<UserDTO> allUserDTOsNotVerified = new ArrayList<>();
        for (User user : userService.findAllByVerified(false)) {
            allUserDTOsNotVerified.add(UserDTO.builder().userType(user.getRoles().toString()).username(user.getUsername()).password(user.getPassword()).fullname(user.getFullname()).schoolClass(user.getSchoolClass()).phoneNumber(user.getPhoneNumber()).subject(user.getSubject()).
                    iban(user.getIban()).address(user.getAddress()).build());
        }
        return allUserDTOsNotVerified;
    }

    @GetMapping("/api/management/users")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> allUserDTOs = new ArrayList<>();
        for (User user : userService.findAll()) {
            allUserDTOs.add(UserDTO.builder().userType(user.getRoles().toString()).username(user.getUsername()).password(user.getPassword()).fullname(user.getFullname()).schoolClass(user.getSchoolClass()).phoneNumber(user.getPhoneNumber()).subject(user.getSubject()).
                    iban(user.getIban()).address(user.getAddress()).build());
        }
        return allUserDTOs;
    }

    @GetMapping("/api/management/attendanceLists")
    public List<Object> getAllAttendanceLists() {
        return new ArrayList<>();
    }

    @GetMapping("/api/management/attendanceList/{id}")
    public Object getAttendanceListById(@PathVariable int id) {
        return new Object();
    }

    @PatchMapping("/api/management/attendanceList/{id}")
    public Object patchAttendanceListById(@RequestBody Object newAttendanceList, @PathVariable int id) {
        return new Object();
    }

    @DeleteMapping("/api/management/attendanceList/{id}")
    public Object deleteAttendanceListById(@RequestBody Object newAttendanceList, @PathVariable int id) {
        return new Object();
    }

    @GetMapping("/api/management/number_of_participants")
    public int getNumberOfParticipants() {
        return 0;
    }

    @PostMapping("api/management/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO, Authentication authentication, Errors errors) {
        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            return new ResponseEntity<>(createErrorString(errors), HttpStatus.BAD_REQUEST);
        }
        if (userService.createAccount(userDTO, authentication) != null)
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        return new ResponseEntity<>("Fehler in Anfrage", HttpStatus.BAD_REQUEST);
    }

    private String createErrorString(Errors errors) {
        return errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
    }
}
