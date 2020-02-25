package com.main.util;

import com.main.dto.interfaces.IUserDTO;
import com.main.service.implementations.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserDTOValidator {
	
	private UserService userService;
	
	public UserDTOValidator(UserService service) {
		this.userService = service;
	}

    private boolean stringIsEmptyOrNull(String element) {
        return element == null || element.isEmpty();
    }

    private void validateUser(IUserDTO userDTO, Errors errors) {
        validateUsername(userDTO.getUsername(), errors);
        validatePassword(userDTO.getPassword(), errors);
        validateFullname(userDTO.getFullname(), errors);
    }


    public void validateUsername(String username, Errors errors) {
        if (stringIsEmptyOrNull(username))
            errors.reject("No username");
        if(userService.findByUsername(username) != null)
        	errors.reject("Username already in use");
    }

    private void validatePassword(String password, Errors errors) {
        if (stringIsEmptyOrNull(password))
        {
            errors.reject("No password");
            return;
        }
        if(password.length() < 7)
        {
            errors.reject("Password length must be larger than 7");
            return;
        }


        //Hat eine Zahl
        int hasOneNumber =  password.matches(".*\\d.*") ? 1 : 0;

        int hasUppercase = !password.equals(password.toLowerCase()) ? 1: 0;
        int hasLowercase = !password.equals(password.toUpperCase()) ? 1 : 0;

        int result = hasOneNumber + hasUppercase + hasLowercase;
        if(result < 2){
            errors.reject("Please follow the password policy");
        }
    }

    private void validateFullname(String fullname, Errors errors) {
        if (stringIsEmptyOrNull(fullname))
            errors.reject("No fullname");
    }

    private void validateEmail(String email, Errors errors) {
        if (stringIsEmptyOrNull(email))
            errors.reject("No email");
        if(userService.findByEmail(email) != null)
        	errors.reject("Email already used");
    }

    private void validatePhoneNumber(String phoneNumber, Errors errors) {
        if (stringIsEmptyOrNull(phoneNumber))
            errors.reject("No phoneNumber");
    }

    private void validateSubject(String subject, Errors errors) {
        if (stringIsEmptyOrNull(subject))
            errors.reject("No subject");
    }

    private void validateIban(String iban, Errors errors) {
        if (stringIsEmptyOrNull(iban))
            errors.reject("No iban");
    }

    private void validateSchoolClass(String schoolClass, Errors errors) {
        if (stringIsEmptyOrNull(schoolClass))
            errors.reject("No schoolClass");
    }

    private void validateAdress(String address, Errors errors) {
        if (stringIsEmptyOrNull(address))
            errors.reject("No address");
    }


    public void validate(IUserDTO userDTO, Errors errors) {
        switch (userDTO.getUserType()) {
            case "USER":
                validateUser(userDTO, errors);
                break;
            case "EMPLOYEE":
                validateUser(userDTO, errors);
                validateEmail(userDTO.getEmail(), errors);
                validatePhoneNumber(userDTO.getPhoneNumber(), errors);
                validateSubject(userDTO.getSubject(), errors);
                validateIban(userDTO.getIban(), errors);
                validateAdress(userDTO.getAddress(), errors);
                break;
            case "MANAGEMENT":
                validateUser(userDTO, errors);
                validateEmail(userDTO.getEmail(), errors);
                validatePhoneNumber(userDTO.getPhoneNumber(), errors);
                validateAdress(userDTO.getAddress(), errors);
                break;
            case "PARENT":
            case "TEACHER":
                validateUser(userDTO, errors);
                validateEmail(userDTO.getEmail(), errors);
                validatePhoneNumber(userDTO.getPhoneNumber(), errors);
                break;
            case "CHILD":
                validateUser(userDTO, errors);
                validateSchoolClass(userDTO.getSchoolClass(), errors);
                break;
            default:
                errors.reject("User Type is missing or wrong");
        }
    }
}
