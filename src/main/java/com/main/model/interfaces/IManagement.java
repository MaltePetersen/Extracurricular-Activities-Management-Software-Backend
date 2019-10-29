package com.main.model.interfaces;

public interface IManagement extends IUser, IContactDetails {

	String getAddress();
	void setAddress(String address);
	
}
