package com.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.VerificationToken;
import com.main.model.interfaces.IVerificationToken;
import com.main.model.userTypes.User;
import com.main.model.userTypes.interfaces.IUser;
import com.main.repository.UserRepository;
import com.main.repository.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	private EmailService emailService;
	private PasswordEncoder passwordEncoder;
	private VerificationTokenRepository verificationTokenRepository;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			VerificationTokenRepository verificationTokenRepository, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.verificationTokenRepository = verificationTokenRepository;
	}

	private ResponseEntity<String> saveUser(@Valid User user) {
		User newUser = userRepository.save(user);
		return new ResponseEntity<String>("Created", newUser == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> save(IUserDTO userDTO, Authentication auth) {
		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

		List<String> roles = new ArrayList<>();
		if (auth != null) {
			auth.getAuthorities().forEach((a) -> {
				roles.add(a.getAuthority());
			});
		}else {
			if(userDTO.getUserType().equals("PARENT"))
				return saveUser(User.UserBuilder.<User>next().withDto(userDTO).withRole(userDTO.getUserType()).build());
		}

		if (userRepository.findByUsername(userDTO.getUsername()) != null)
			return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);

		if (roles.contains("ROLE_MANAGEMENT")) {
			return saveUser(User.UserBuilder.<User>next().withDto(userDTO).withRole(userDTO.getUserType()).build());
		} else if (roles.contains("ROLE_PARENT") && userDTO.getUserType().equals("CHILD")) {
			return saveUser(User.UserBuilder.<User>next().withDto(userDTO).withRole(userDTO.getUserType()).build());
		} else if (roles.contains("ROLE_SCHOOLCOORDINATOR")
				&& (userDTO.getUserType().equals("USER") || userDTO.getUserType().equals("TEACHER"))) {
			return saveUser(User.UserBuilder.<User>next().withDto(userDTO).withRole(userDTO.getUserType()).build());
		}

		return new ResponseEntity<>("Role not valid", HttpStatus.CONFLICT);
	}

	public void update(User user) {
		userRepository.save(user);
	}

	@Override
	public IUser createAccount(UserDTO userDTO, Authentication auth) {
		ResponseEntity<String> response = save(userDTO, auth);
		if (response.getStatusCode().equals(HttpStatus.CREATED)) {
			return findByEmail(userDTO.getEmail());
		}
		return null;
	}

	@Override
	public IVerificationToken createVerificationToken(User user, String token) {
		return verificationTokenRepository.save(new VerificationToken(token, user));

	}

	@Override
	public IUser findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public boolean emailExist(String email) {
		return findByEmail(email) != null;
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	public List<User> findAll() {
		return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public IUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
    public User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("user id not found: " + id));
    }
}
