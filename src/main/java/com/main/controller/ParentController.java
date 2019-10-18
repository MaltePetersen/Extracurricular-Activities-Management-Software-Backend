package com.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParentController {

	@GetMapping("/api/parent")
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
}
