package com.main.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.main.dto.UserDTO;
import com.main.model.interfaces.IVerificationToken;
import com.main.model.userTypes.User;
import com.main.repository.VerificationTokenRepository;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;
import com.main.util.register.OnRegistrationCompleteEvent;

@RestController
public class UserController {

	private UserService userService;
	private VerificationTokenRepository verificationTokenRepository;
	private UserDTOValidator userDTOValidator;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	public UserController(UserService userService, VerificationTokenRepository verificationTokenRepository,
			UserDTOValidator userDTOValidator) {
		this.userService = userService;
		this.verificationTokenRepository = verificationTokenRepository;
		this.userDTOValidator = userDTOValidator;
	}

	@PostMapping("/register")
	public ResponseEntity<String> registration(@RequestBody UserDTO userDTO, Authentication auth, Errors errors,
			WebRequest request) {
		userDTOValidator.validate(userDTO, errors);
		if (errors.hasErrors()) {
			return new ResponseEntity<>(createErrorString(errors), HttpStatus.BAD_REQUEST);
		}

		User registered = userService.createAccount(userDTO, auth);

		try {
			String appUrl = request.getContextPath();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
		} catch (Exception e) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Created: " + registered.getRole(), HttpStatus.CREATED);
	}

	private String createErrorString(Errors errors) {
		return errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public ResponseEntity<String> confirmRegistration(WebRequest request, @RequestParam("token") String token) {

		// Locale locale = request.getLocale();

		IVerificationToken verificationToken = verificationTokenRepository.findByToken(token);
		if (verificationToken == null) {
			return new ResponseEntity<>("Verification Token is null", HttpStatus.BAD_REQUEST);
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			verificationToken.reset();
			return new ResponseEntity<>("Token is already expired", HttpStatus.BAD_REQUEST);
		}

		user.setEnabled(true);
		userService.update(user);
		return new ResponseEntity<>("User is confirmed", HttpStatus.ACCEPTED);
	}

	
	
}
