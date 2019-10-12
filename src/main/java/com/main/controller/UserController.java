package com.main.controller;


import com.main.dto.UserDTO;

import com.main.model.VerificationToken;
import com.main.model.userTypes.User;
import com.main.repository.VerificationTokenRepository;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.HashMap;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;
    private VerificationTokenRepository verificationTokenRepository;
    private UserDTOValidator userDTOValidator;

    UserController(UserService userService, VerificationTokenRepository verificationTokenRepository, UserDTOValidator userDTOValidator) {
        this.userService = userService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userDTOValidator = userDTOValidator;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody UserDTO user, Authentication auth, Errors errors) {
        userDTOValidator.validate(user, errors);
        if (errors.hasErrors()) {
            return new ResponseEntity<>(createErrorString(errors), HttpStatus.BAD_REQUEST);
        }
        return userService.save(user, auth);
    }

    private String createErrorString(Errors errors) {
        return errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @RequestMapping(value = "/regitrationConfirm", method = RequestMethod.GET)
    public ResponseEntity<String> confirmRegistration(WebRequest request, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return new ResponseEntity<>("Verification Token is null", HttpStatus.BAD_REQUEST);
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new ResponseEntity<>("Token is already expired", HttpStatus.BAD_REQUEST);
        }

        user.setEnabled(true);
        userService.update(user);
        return new ResponseEntity<>("User is confirmed", HttpStatus.ACCEPTED);
    }
}
