package com.main.data;

import com.main.model.Privilege;

public enum TestUserControllerPath {
	REGISTER("/user/register"), RESENDTOKEN("/user/resendToken"), EMAILCONFIRMATION("/user/registrationConfirm?token="), RESETPASSWORD("/user/resetPassword"), LOGIN("/user/login");

	private String path;
	
	public String getUri() {
		return path;
	}
	
	private TestUserControllerPath(String path) {
		this.path = path;
	}
}
