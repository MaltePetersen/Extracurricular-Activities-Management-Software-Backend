package com.main.controller;

import com.main.dto.SchoolDTO;
import com.main.dto.converters.SchoolConverter;
import com.main.model.School;
import com.main.model.interfaces.ISchool;
import com.main.service.implementations.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class SchoolController {

	private SchoolService service;

    SchoolController(SchoolService service) {
        this.service = service;
    }

//    // Index
//    @PreAuthorize("hasAuthority('ROLE')")  
	@GetMapping("/api/schools")
	public List<SchoolDTO> getSchools() {
	    return service.getAll().stream().map(SchoolConverter::toDto).collect(Collectors.toList());
	}

	// Save
	@PostMapping("/api/schools")
	@ResponseStatus(HttpStatus.CREATED)
	ISchool newSchool(@RequestBody School newSchool) {
		return service.save(newSchool);
	}

	// Find
    @GetMapping("/api/schools/{id}")
    ISchool findOne(@PathVariable @Min(1) Long id) {
        return service.findOne(id);
    }


    // Update school entry
    @PatchMapping("/api/schools/{id}")
    ISchool patch(@RequestBody School newSchool, @PathVariable Long id) {
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
