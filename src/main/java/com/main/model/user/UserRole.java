package com.main.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
	ROLE_MANAGEMENT, ROLE_PARENT, ROLE_CHILD, ROLE_SCHOOLCOORDINATOR, ROLE_EMPLOYEE, ROLE_TEACHER, ROLE_USER,
	ROLE_RESET_PASSWORD;

	@Override
	public String getAuthority() {
		return toString();
	}

	public static UserRole byRole(String role) {
		return UserRole.valueOf("ROLE_" + role);
	}

}