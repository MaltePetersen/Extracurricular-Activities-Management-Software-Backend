package com.main.service;

import com.main.dto.UserDTO;
import com.main.model.userTypes.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface UserService {
    public ResponseEntity<String> save(UserDTO userDTO, Authentication auth);
    public void update(User user);
}

