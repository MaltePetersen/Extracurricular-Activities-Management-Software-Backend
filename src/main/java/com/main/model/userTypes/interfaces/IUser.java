package com.main.model.userTypes.interfaces;

import com.main.model.userTypes.UserAuthority;

public interface IUser {
	
	Long getId();
	void setId(Long id);
	
	String getPassword();
	void setPassword(String password);
	
	String getUsername();
	void setUsername(String username);
	
	String getFullname();
	void setFullname(String fullname);

	String getRole();
	void setRole(String role);
	
	void addAuthority(UserAuthority authority);

	void setVerified(boolean verified);
	boolean isVerified();
}
