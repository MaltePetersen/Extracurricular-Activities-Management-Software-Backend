package com.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.model.Activeness;
import com.main.service.ActivenessService;

//Experimenteller Controller

@RestController
public class ActivenessController {

	@Autowired
	private ActivenessService service;

    @CrossOrigin
	@GetMapping("/api/activeness")
	public List<Activeness> getActiveness(Authentication authentication) {
        for (GrantedAuthority a:
        authentication.getAuthorities()) {
            System.out.println(a.getAuthority());
        }

	    return service.getAll();
	}
}
