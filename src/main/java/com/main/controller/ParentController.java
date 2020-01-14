package com.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.main.dto.UserDTO;

@RestController
@CrossOrigin
public class ParentController {
	
	@PostMapping("/api/parent/addChild")
	public ResponseEntity<String> addChild(@RequestBody UserDTO userDTO, Authentication auth){
		return new ResponseEntity<String>("", HttpStatus.ACCEPTED);
	}
	
}
