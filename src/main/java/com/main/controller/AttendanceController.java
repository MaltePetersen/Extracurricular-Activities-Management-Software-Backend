package com.main.controller;

import com.main.service.AttendanceService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AttendanceController {

    private AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;

    }
}
