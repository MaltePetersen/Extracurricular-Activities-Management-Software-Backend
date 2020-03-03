package com.main.dto.interfaces;

public interface IUserDTO {

	String getUserType();

	void setUserType(String userType);

	String getUsername();

	void setUsername(String username);

	String getPassword();

	void setPassword(String password);

	String getEmail();

	void setEmail(String email);

	String getFullname();

	void setFullname(String fullname);

	String getSchoolClass();

	void setSchoolClass(String schoolClass);

	/*Long getChildSchool();

	//childSchoolID
	void setChildSchool(Long childSchool);*/

	String getPhoneNumber();

	void setPhoneNumber(String phoneNumber);

	String getSubject();

	void setSubject(String subject);

	String getIban();

	void setIban(String iban);

	String getAddress();

	void setAddress(String address);

	boolean isSchoolCoordinator();

	void setSchoolCoordinator(boolean isSchoolCoordinator);

	Long getSchool();

	void setSchool(Long school);

}