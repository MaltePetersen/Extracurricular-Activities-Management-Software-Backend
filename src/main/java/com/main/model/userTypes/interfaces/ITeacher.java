package com.main.model.userTypes.interfaces;

public interface ITeacher extends IUser, IContactDetails, ISchoolCoordinator {
	
	String getSubject();
	void setSubject(String subject);
	
}
