package com.main.service.implementations;

import java.util.List;

import com.main.model.user.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.interfaces.IVerificationToken;

public interface UserService {
    ResponseEntity<String> save(IUserDTO userDTO, Authentication auth);
    User update(User user);
    User update(User user, UserRole role);
	IUser createAccount(UserDTO userDTO, Authentication auth);
	IVerificationToken createVerificationToken(User user, String token);
	IUser findByEmail(String email);
	boolean emailExist(String email);
	void deleteUser(User user);
	void deleteUserById(long id);
	List<User> findAll();
	IUser findByUsername(String username);
    User findOne(Long id);
	String changePassword(IUser user);
	List<User> findAllByVerified(boolean b);
    UserDTO toDto(IUser user);

    void deleteByName(String username);
    void changePassword(IUser user, String password);

	void changeEmail(IUser user, String email);
}

