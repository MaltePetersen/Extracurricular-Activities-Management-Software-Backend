package com.main.model.user;

import com.main.model.Privilege;
import org.springframework.security.core.GrantedAuthority;

public enum UserPrivilege implements GrantedAuthority {
	RESET_PASSWORD, RESET_TOKEN, RESET_CHILD_PASSWORD;

    @Override
	public String getAuthority() {
		return toString();
	}
	
	public static UserPrivilege byRole(String role) {
		return UserPrivilege.valueOf("ROLE_" + role);
	}
}
