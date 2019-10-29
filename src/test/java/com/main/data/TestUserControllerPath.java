package com.main.data;

public enum TestUserControllerPath {
	REGISTER("/register"), RESENDTOKEN("/resendToken"), EMAILCONFIRMATION("/registrationConfirm?token="), RESETPASSWORD("/resetPassword");
	
	private String path;
	
	public String getUri() {
		return path;
	}
	
	private TestUserControllerPath(String path) {
		this.path = path;
	}
}
