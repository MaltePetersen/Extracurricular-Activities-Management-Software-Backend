package com.main.util.events;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.main.model.interfaces.IContactDetails;
import com.main.model.interfaces.IUser;
import com.main.service.EmailService;
import com.main.service.UserService;

@Component
@Log
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

	private EmailService emailService;
	private UserService userService;

	public ResetPasswordListener(UserService userService, EmailService emailService) {
		super();
		this.userService = userService;
		this.emailService = emailService;
	}

	
	@Override
	public void onApplicationEvent(OnResetPasswordEvent event) {
		compute(event);	
	}

	private void compute(OnResetPasswordEvent event) {
		IUser user = userService.findByEmail(event.getEmail());
		IContactDetails contactDetails = (IContactDetails) user;
		
		String newPassword = userService.changePassword(user);
		System.out.println("Neues Passwort lautet: " + newPassword);
		String email = contactDetails.getEmail();
		
		emailService.sendSimpleMessage(email, "Passwort zurücksetzen" , newPassword);
		log.info("Sending an email to " + email + " in event: " + ResetPasswordListener.class.getName());
	}
}
