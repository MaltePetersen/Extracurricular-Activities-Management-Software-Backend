package com.main.model.interfaces;

import java.util.List;
import java.util.Set;

import com.main.model.Role;

public interface IUser {
	
	Long getId();
	void setId(Long id);
	
	String getPassword();
	void setPassword(String password);
	
	String getUsername();
	void setUsername(String username);
	
	String getFullname();
	void setFullname(String fullname);

	List<Role> getRoles();
	void setRoles(List<Role> role);
	
	void setVerified(boolean verified);
	boolean isVerified();
}
