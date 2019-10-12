package com.main.controller;

import javax.validation.Valid;

import com.main.dto.UserDTO;
import com.main.model.userTypes.User;
import com.main.repository.UserRepository;
import com.main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registration(@Valid @RequestBody UserDTO user, Authentication auth) {
        return userService.save(user,auth);
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
