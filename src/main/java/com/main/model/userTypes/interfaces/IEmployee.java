package com.main.model.userTypes.interfaces;

import java.util.List;

import com.main.model.School;

public interface IEmployee extends IUser, IContactDetails, ITeacher {

	String getAddress();
	void setAddress(String address);
	
	String getIban();
	void setIban(String adress);
	
	List<School> getEmployeesSchools();
	void setEmployeesSchools(List<School> employeesSchools);
	
}
