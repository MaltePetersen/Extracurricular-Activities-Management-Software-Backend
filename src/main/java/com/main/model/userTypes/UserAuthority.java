package com.main.model.userTypes;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority{
	ROLE_MANAGEMENT, ROLE_PARENT, ROLE_CHILD, ROLE_SCHOOLCOORDINATOR, ROLE_EMPLOYEE, ROLE_TEACHER,
	RESET_PASSWORD, ROLE_USER, RESET_TOKEN;

	@Override
	public String getAuthority() {
		return toString();
	}
	
}