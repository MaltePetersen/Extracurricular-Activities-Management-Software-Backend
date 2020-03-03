package com.main.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
	ROLE_MANAGEMENT(0), ROLE_PARENT(1), ROLE_CHILD(2), ROLE_SCHOOLCOORDINATOR(3), ROLE_EMPLOYEE(4), ROLE_TEACHER(5), ROLE_USER(6),
	ROLE_RESET_PASSWORD(7);

	private int id;

	private UserRole(int id){
		this.id = id;
	}

	@Override
	public String getAuthority() {
		return toString();
	}

	public static UserRole byRole(String role) {
		return UserRole.valueOf("ROLE_" + role);
	}

}