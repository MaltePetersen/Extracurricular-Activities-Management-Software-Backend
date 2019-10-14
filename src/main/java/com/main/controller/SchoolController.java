package com.main.controller;

import com.main.model.School;
import com.main.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class SchoolController {

	@Autowired
	private SchoolService service;

	// Index
	@GetMapping("/api/schools")
	public List<School> getSchools() {
	    return service.getAll();
	}

	// Save
	@PostMapping("/api/schools")
	@ResponseStatus(HttpStatus.CREATED)
	School newSchool(@RequestBody School newSchool) {
		return service.save(newSchool);
	}

	// Find
    @GetMapping("/api/schools/{id}")
    School findOne(@PathVariable @Min(1) Long id) {
        return service.findOne(id);
    }


    // Update school entry
    @PatchMapping("/api/schools/{id}")
    School patch(@RequestBody School newSchool, @PathVariable Long id) {
        School school = service.findOne(id);

        school.setName(newSchool.getName());
        school.setAddress(newSchool.getAddress());
        school.setEmail(newSchool.getEmail());
        school.setPhoneNumber(newSchool.getPhoneNumber());

        service.save(school);

        return school;
    }

    // Delete
    @DeleteMapping("/api/schools/{id}")
    void deleteSchool(@PathVariable Long id) {
        service.deleteById(id);
    }
}
