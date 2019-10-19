package com.main.util.register;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.main.model.userTypes.User;
import com.main.service.EmailService;
import com.main.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

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
	
	}

}
