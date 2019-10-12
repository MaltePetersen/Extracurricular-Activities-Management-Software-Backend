package com.main.util;

import com.main.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserDTOValidator {

    private boolean stringIsEmptyOrNull(String element) {
        return element == null || element.isEmpty();
    }

    private void validateUser(UserDTO userDTO, Errors errors) {
        validateUsername(userDTO.getUsername(), errors);
        validatePassword(userDTO.getPassword(), errors);
        validateFullname(userDTO.getFullname(), errors);
    }


    private void validateUsername(String username, Errors errors) {
        if (stringIsEmptyOrNull(username))
            errors.reject("No username");
    }

    private void validatePassword(String password, Errors errors) {
        if (stringIsEmptyOrNull(password))
            errors.reject("No password");
    }

    private void validateFullname(String fullname, Errors errors) {
        if (stringIsEmptyOrNull(fullname))
            errors.reject("No fullname");
    }

    private void validateEmail(String email, Errors errors) {
        if (stringIsEmptyOrNull(email))
            errors.reject("No email");
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

    private void validateaddress(String address, Errors errors) {
        if (stringIsEmptyOrNull(address))
            errors.reject("No address");
    }


    public void validate(UserDTO userDTO, Errors errors) {
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
                validateaddress(userDTO.getAddress(), errors);
                break;
            case "MANAGEMENT":
                validateUser(userDTO, errors);
                validateEmail(userDTO.getEmail(), errors);
                validatePhoneNumber(userDTO.getPhoneNumber(), errors);
                validateaddress(userDTO.getAddress(), errors);
                break;
            case "PARENT":
                validateUser(userDTO, errors);
                validateEmail(userDTO.getEmail(), errors);
                validatePhoneNumber(userDTO.getPhoneNumber(), errors);
                break;
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
