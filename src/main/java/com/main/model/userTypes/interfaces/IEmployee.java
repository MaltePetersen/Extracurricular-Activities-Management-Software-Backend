package com.main.model.userTypes.interfaces;

public interface IEmployee extends IUser, IContactDetails, ITeacher {

	String getAddress();
	void setAddress(String address);
	
	String getIban();
	void setIban(String adress);
	
}
