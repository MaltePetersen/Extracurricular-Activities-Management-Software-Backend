package com.main.controller;

import com.main.dto.UserDTO;
import com.main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class ManagementController {
    private UserService userService;

    public ManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/admin/not_enabled_users")
    public List<UserDTO> allNotEnabledUser() {
        return new ArrayList<>();
    }

    @GetMapping("/api/admin/users")
    public List<UserDTO> getAllUsers() {
        return new ArrayList<>();
    }

    @GetMapping("/api/admin/attendanceLists")
    public List<Object> getAllAttendanceLists() {
        return new ArrayList<>();
    }

    @GetMapping("/api/admin/attendanceList/{id}")
    public Object getAttendanceListById(@PathVariable int id) {
        return new Object();
    }

    @PatchMapping("/api/admin/attendanceList/{id}")
    public Object patchAttendanceListById(@RequestBody Object newAttendanceList, @PathVariable int id) {
        return new Object();
    }

    @DeleteMapping("/api/admin/attendanceList/{id}")
    public Object deleteAttendanceListById(@RequestBody Object newAttendanceList, @PathVariable int id) {
        return new Object();
    }
    @GetMapping("/api/admin/number_of_participants")
    public int getNumberOfParticipants(){
        return 0;
    }
    @PostMapping("api/admin/register")
    public ResponseEntity<String> register(){
        return new ResponseEntity<>("User erstellt", HttpStatus.ACCEPTED);
    }

}
