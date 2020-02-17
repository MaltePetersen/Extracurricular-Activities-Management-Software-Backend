package com.main.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.main.dto.UserDTO;
import com.main.model.User;
import com.main.model.VerificationToken;
import com.main.model.interfaces.IContactDetails;
import com.main.model.interfaces.IUser;
import com.main.model.interfaces.IVerificationToken;
import com.main.repository.VerificationTokenRepository;
import com.main.service.implementations.UserService;
import com.main.util.UserDTOValidator;
import com.main.util.events.OnRegistrationCompleteEvent;
import com.main.util.events.OnResetPasswordEvent;

import lombok.extern.java.Log;

import javax.transaction.Transactional;
import javax.validation.constraints.Email;

@CrossOrigin
@RestController
@Log
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private VerificationTokenRepository verificationTokenRepository;
    private UserDTOValidator userDTOValidator;
    private ApplicationEventPublisher eventPublisher;

    public UserController(UserService userService, VerificationTokenRepository verificationTokenRepository,
                          UserDTOValidator userDTOValidator, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userDTOValidator = userDTOValidator;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody UserDTO userDTO, Authentication auth, Errors errors,
                                               WebRequest request) {
        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            return new ResponseEntity<>(createErrorString(errors), HttpStatus.BAD_REQUEST);
        }

        IUser registered = userService.createAccount(userDTO, auth);

        if (registered == null)
            return new ResponseEntity<>("Error creating the account", HttpStatus.BAD_REQUEST);

        try {
            String appUrl = request.getContextPath();
            eventPublisher
                    .publishEvent(new OnRegistrationCompleteEvent((User) registered, request.getLocale(), appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Fehler beim Versenden", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Created: " + registered.getRoles(), HttpStatus.CREATED);
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
            return new ResponseEntity<>("Token is already expired", HttpStatus.BAD_REQUEST);
        }

        user.setVerified(true);
        userService.update(user);
        return new ResponseEntity<>("User is confirmed", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/resendToken", method = RequestMethod.GET)
    public ResponseEntity<String> resendToken(Authentication auth, WebRequest request) {
        if (auth == null)
            return new ResponseEntity<>("Not Authentificated", HttpStatus.BAD_REQUEST);
        IUser user = userService.findByUsername(auth.getName());

        if (user == null)
            return new ResponseEntity<>("Fehler beim Versenden", HttpStatus.BAD_REQUEST);

        try {
            String appUrl = request.getContextPath();
            IVerificationToken token = verificationTokenRepository
                    .findByUser_Email(((IContactDetails) user).getEmail());
            verificationTokenRepository.delete((VerificationToken) token);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent((User) user, request.getLocale(), appUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("The token was sent again", HttpStatus.ACCEPTED);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody(required = false) Map<String, Object> emailMap, WebRequest request, Authentication authentication) {
        log.info(authentication.getName() + " tries to change the password with data: " + emailMap.toString());

        List<String> authorities = new ArrayList<GrantedAuthority>(authentication.getAuthorities()).stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        boolean changeAllPasswords = authorities.contains("ROLE_MANAGEMENT");

        Object obj = emailMap.get("email");

        if (obj == null)
            return new ResponseEntity<>("Please enter a real email", HttpStatus.BAD_REQUEST);

        @Email
        String email = obj.toString();

        if (email.length() == 0)
            return new ResponseEntity<>("Please enter a real email", HttpStatus.BAD_REQUEST);

        try {
            String appUrl = request.getContextPath();
            IUser user = userService.findByEmail(email);
            if (user == null) {
                return new ResponseEntity<>("Error while sending the message", HttpStatus.BAD_REQUEST);
            }

            if (!changeAllPasswords && user.getUsername().equals(authentication.getName())) {
                eventPublisher
                        .publishEvent(new OnResetPasswordEvent(email, request.getLocale(), appUrl));
            } else if (changeAllPasswords) {
                eventPublisher
                        .publishEvent(new OnResetPasswordEvent(email, request.getLocale(), appUrl));

            } else {
                return new ResponseEntity<>("Error while sending the message", HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Error while sending the message", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("A new Password was sent to the email", HttpStatus.ACCEPTED);
    }


    @CrossOrigin
    @GetMapping("/login")
    public List<String> login(Authentication auth) {
        log.info("User " + auth.getName() + " logs in");
        List<String> list = new ArrayList<>();
        auth.getAuthorities().forEach((a) -> list.add(a.getAuthority()));
        return list;
    }


    @CrossOrigin
    @GetMapping("/profile")
    public UserDTO getUserByAuth(Authentication auth) {
        if (auth == null)
            return null;
        IUser user = userService.findByUsername(auth.getName());
        if (user == null)
            return null;
        return userService.toDto(user);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserDTO> patchUserByAuth(@RequestParam UserDTO userDTO,
                                   Authentication authentication){
        if(authentication == null)
            return ResponseEntity.status(403).build();

        String username = userDTO.getUsername();
        String email = userDTO.getEmail();

        IUser user = userService.findByUsername(authentication.getName());

        if(username != null){
            if(!username.equals(user.getUsername())){
                user.setUsername(username);
            }
        }

        if(email != null){
            if(email.length() > 6)
                userService.changeEmail(user, email);
        }

        return ResponseEntity.ok( userService.toDto(user) );

    }



}
