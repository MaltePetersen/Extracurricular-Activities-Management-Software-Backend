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
import com.main.model.afterSchoolCare.AfterSchoolCare;

@RestController
@RequestMapping("/api/parent")
@CrossOrigin
public class ParentController {	
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
    public List<UserDTO> getChilds() {
        return childs;
    }

    @PostMapping("/child")
    @ResponseStatus(HttpStatus.CREATED)
    void createChild(@RequestBody UserDTO child) {
        childs.add(child);
    }

    @GetMapping("/child/{id}")
    UserDTO getChild(@PathVariable @Min(1) int id) {
        return childs.get(id-1);
    }

    @PatchMapping("/child/{id}")
    void changeChild(@RequestBody UserDTO child, @PathVariable int id) {
        childs.set(id -1, child);

    }

    @DeleteMapping("/child/{id}")
    void deleteChild(@PathVariable int id) {
        childs.remove(id -1);
    }
		
}
