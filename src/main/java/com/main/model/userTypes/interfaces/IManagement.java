package com.main.model.userTypes.interfaces;

public interface IManagement extends IUser, IContactDetails {

	String getAddress();
	void setAddress(String address);
	
}
