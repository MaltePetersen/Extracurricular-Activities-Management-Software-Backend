package com.main.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.main.dto.UserDTO;
import com.main.model.AfterSchoolCare;

@RestController
@RequestMapping("/api/parent")
@CrossOrigin
public class ParentController { 
	//For what do we need this controller?	--> IsParent hält nicht REST Paradigmen ein
	@GetMapping("/authority")
	public ResponseEntity<Void> isParent(Authentication auth) {
		if (auth != null) {
			List<String> roles = new ArrayList<>();
			if (auth != null)
				auth.getAuthorities().forEach((a) -> {
					roles.add(a.getAuthority());
				});
			if(roles.contains("ROLE_PARENT")) {
				return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
			}

		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
@GetMapping("/bookedafterschoolcares")
	public String getBookedAfterSchoolCares(){
		return "Not yet implemented";
	}
    @PostMapping("/bookedafterschoolcare")
    @ResponseStatus(HttpStatus.CREATED)
    String createBookedAfterSchoolCare(@RequestBody AfterSchoolCare AfterSchoolCare) {
        return "Not yet implemented";
    }
    @PatchMapping("/bookedafterschoolcare/{id}")
    String changeBookedAfterSchoolCare(@RequestBody AfterSchoolCare AfterSchoolCare, @PathVariable Long id) {
        return "Not yet implemented";
    }
    @DeleteMapping("/bookedafterschoolcare/{id}")
    String deleteBookedAfterSchoolCare(@PathVariable Long id) {
        return "Not yet implemented";
	}
	
	@GetMapping("/afterschoolcares")
	public String getAfterSchoolCares(){
		return "Not yet implemented";
	}
	
    @GetMapping("/afterschoolcare/{id}")
    public String getAfterSchoolCare(@PathVariable int id) {
        return "Not yet implemented";
	}
	
	@GetMapping("/childs")
    public String getChilds() {
        return "Not yet implemented";
    }

    @PostMapping("/child")
    @ResponseStatus(HttpStatus.CREATED)
    String createChild(@RequestBody AfterSchoolCare AfterSchoolCare) {
        return "Not yet implemented";
    }

    @GetMapping("/child/{id}")
    String getChild(@PathVariable @Min(1) Long id) {
        return "Not yet implemented";
    }

    @PatchMapping("/child/{id}")
    String changeChild(@RequestBody AfterSchoolCare AfterSchoolCare, @PathVariable Long id) {
        return "Not yet implemented";

    }

    @DeleteMapping("/child/{id}")
    String deleteChild(@PathVariable Long id) {
        return "Not yet implemented";
    }
		
}
