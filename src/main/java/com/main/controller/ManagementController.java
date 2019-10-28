package com.main.controller;

import com.main.dto.UserDTO;
import com.main.model.userTypes.User;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<User> allNotEnabledUser() {
        return userService.findAllByVerified(false);
    }

    @GetMapping("/api/management/users")
    public List<User> getAllUsers() {
        return userService.findAll();
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
