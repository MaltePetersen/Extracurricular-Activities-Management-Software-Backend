package com.main.util.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.main.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 1L;
	
	private String appUrl;
	private Locale locale;
	private User user;
	
	public OnRegistrationCompleteEvent(Object source) {
		super(source);
	}

	public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	
	
}
