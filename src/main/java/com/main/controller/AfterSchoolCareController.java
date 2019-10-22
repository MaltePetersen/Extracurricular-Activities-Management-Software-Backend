package com.main.controller;

import com.main.model.AfterSchoolCare;
import com.main.service.AfterSchoolCareService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class AfterSchoolCareController {

    private AfterSchoolCareService service;

    AfterSchoolCareController(AfterSchoolCareService service) {
        this.service = service;
    }

    // Index
    @GetMapping("/api/after_school_cares")
    public List<AfterSchoolCare> getAfterSchoolCares() {
        return service.getAll();
    }

    // Save AfterSchoolCare
    @PostMapping("/api/after_school_cares")
    @ResponseStatus(HttpStatus.CREATED)
    AfterSchoolCare newAfterSchoolCare(@RequestBody AfterSchoolCare newAfterSchoolCare) {
        return service.save(newAfterSchoolCare);
    }

    // Find
    @GetMapping("/api/after_school_cares/{id}")
    AfterSchoolCare findOne(@PathVariable @Min(1) Long id) {
        return service.findOne(id);
    }

    // Update AfterSchoolCare entry
    @PatchMapping("/api/after_school_cares/{id}")
    AfterSchoolCare patch(@RequestBody AfterSchoolCare newCareService, @PathVariable Long id) {
        AfterSchoolCare afterSchoolCare = service.findOne(id);

        // TODO: attributes to be implemented

        service.save(afterSchoolCare);

        return afterSchoolCare;
    }

    // Delete
    @DeleteMapping("/api/after_school_cares/{id}")
    void deleteSchool(@PathVariable Long id) {
        service.deleteById(id);
    }
}
