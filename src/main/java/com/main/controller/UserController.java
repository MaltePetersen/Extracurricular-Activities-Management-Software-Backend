package com.main.controller;

import javax.validation.Valid;

import com.main.dto.UserDTO;
import com.main.model.VerificationToken;
import com.main.model.userTypes.Parent;
import com.main.model.userTypes.User;
import com.main.repository.UserRepository;
import com.main.repository.VerificationTokenRepository;
import com.main.service.EmailService;
import com.main.service.UserService;
import com.main.util.UserDTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
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
    UserController(UserService userService, VerificationTokenRepository verificationTokenRepository,UserDTOValidator userDTOValidator ) {
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
        return userService.save(user,auth);
    }

    private String createErrorString(Errors errors) {
        return errors.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(","));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Member")
    public class MemberNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 10443642453485954L;

        public MemberNotFoundException() {
            super("No such Member found");
        }

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
}
