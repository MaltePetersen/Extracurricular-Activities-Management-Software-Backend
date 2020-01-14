package com.main.util.events;

import java.util.UUID;

import lombok.extern.java.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.main.model.User;
import com.main.service.EmailService;
import com.main.service.UserService;

@Component
@Log
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	private EmailService emailService;
	private UserService userService;

	public RegistrationListener(UserService userService, EmailService emailService) {
		super();
		this.userService = userService;
		this.emailService = emailService;
	}

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		compute(event);
	}

	private void compute(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();

		userService.createVerificationToken(user, token);

		String email = user.getEmail();
		String subject = "Bestätigungsemail";
		String confirmationUrl = event.getAppUrl() + "/regitrationConfirm.html?token=" + token;
		String message = "Bitte bestätigen Sie ihre Identität indem Sie auf den folgenden Link klicken: \n";
		message += confirmationUrl;
		
		emailService.sendSimpleMessage(email, subject, message);
		log.info("Sending an email to " + email + " in event: " + RegistrationListener.class.getName());
	}

}
