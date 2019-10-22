package com.main.service;

import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.VerificationToken;
import com.main.model.userTypes.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface UserService {
    public ResponseEntity<String> save(IUserDTO userDTO, Authentication auth);
    public void update(User user);
	public User createAccount(UserDTO userDTO, Authentication auth);
	public VerificationToken createVerificationToken(User user, String token);
	public User findByEmail(String email);
	public boolean emailExist(String email);
	public void deleteUser(User user);
}

