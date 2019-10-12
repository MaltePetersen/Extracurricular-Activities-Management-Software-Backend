package com.main.service;

import com.main.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<String> save(UserDTO userDTO);
}

