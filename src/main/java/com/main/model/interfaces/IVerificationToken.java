package com.main.model.interfaces;

import java.util.Date;

import com.main.model.userTypes.User;

public interface IVerificationToken {

	Long getId();

	void setId(Long id);

	String getToken();

	void setToken(String token);

	User getUser();

	void setUser(User user);

	Date getExpiryDate();

	void setExpiryDate(Date expiryDate);

	void reset();

}