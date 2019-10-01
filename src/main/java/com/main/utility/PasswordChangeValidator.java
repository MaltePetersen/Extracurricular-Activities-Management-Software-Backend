package com.main.utility;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.main.model.PasswordChange;

@Component
public class PasswordChangeValidator implements Validator {


	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordChange.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PasswordChange passwordChange = (PasswordChange) target;
	
		if(!passwordChange.getPassword().equals(passwordChange.getConfirmPassword())) {
			errors.reject("password", "password.notequals");
		}
	

	}

}
