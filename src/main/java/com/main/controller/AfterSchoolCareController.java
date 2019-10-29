package com.main.controller;

import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.converters.AfterSchoolCareConverter;
import com.main.model.AfterSchoolCare;
import com.main.model.Attendance;
import com.main.service.AfterSchoolCareService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin
@RestController
public class AfterSchoolCareController {

	private AfterSchoolCareService service;

	public AfterSchoolCareController(AfterSchoolCareService service) {
		this.service = service;

	}

	// Index
	@GetMapping("/api/after_school_cares")
	public List<AfterSchoolCareDTO> getAfterSchoolCares() {
		return service.getAll().stream().map(AfterSchoolCareConverter::toDto)
				.collect(Collectors.toList());
	}

	// Save AfterSchoolCare
	@PostMapping("/api/after_school_cares")
	@ResponseStatus(HttpStatus.CREATED)
	AfterSchoolCareDTO newAfterSchoolCare(@RequestBody AfterSchoolCare newAfterSchoolCare) {
		return AfterSchoolCareConverter.toDto(service.save(newAfterSchoolCare));
	}

	// Find
	@GetMapping("/api/after_school_cares/{id}")
	AfterSchoolCareDTO findOne(@PathVariable @Min(1) Long id) {
		return AfterSchoolCareConverter.toDto(service.findOne(id));
	}

	// Update AfterSchoolCare entry
	@PatchMapping("/api/after_school_cares/{id}")
	AfterSchoolCareDTO patch(@RequestBody AfterSchoolCare newCareService, @PathVariable Long id) {
		AfterSchoolCare afterSchoolCare = service.findOne(id);

		// TODO: attributes to be implemented

		service.save(afterSchoolCare);

		return AfterSchoolCareConverter.toDto(afterSchoolCare);
	}

	@PatchMapping("/api/after_school_cares/{id}/attendance")
	AfterSchoolCare patch(@RequestBody Attendance attendance, @PathVariable Long id) {
		return service.addAttendance(id, attendance);
	}

	// Delete
	@DeleteMapping("/api/after_school_cares/{id}")
	void deleteAfterSchoolCare(@PathVariable Long id) {
		service.deleteById(id);
	}
}
