package com.main.util.register;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.main.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnResetPasswordEvent extends ApplicationEvent {

	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 1337L;

	private String appUrl;
	private Locale locale;
	private String email;
	
	public OnResetPasswordEvent(Object source) {
		super(source);
	}
	
	public OnResetPasswordEvent(String email, Locale locale, String appUrl) {
		super(email);
		this.email = email;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	
}
