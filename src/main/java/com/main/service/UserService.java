package com.main.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.interfaces.IVerificationToken;
import com.main.model.userTypes.User;
import com.main.model.userTypes.interfaces.IUser;

public interface UserService {
    public ResponseEntity<String> save(IUserDTO userDTO, Authentication auth);
    public void update(User user);
	public IUser createAccount(UserDTO userDTO, Authentication auth);
	public IVerificationToken createVerificationToken(User user, String token);
	public IUser findByEmail(String email);
	public boolean emailExist(String email);
	public void deleteUser(User user);
	public List<User> findAll();
	public IUser findByUsername(String username);
}

