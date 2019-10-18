package com.main.model.interfaces;

import java.util.List;

import com.main.model.userTypes.User;

public interface ISchool {

	Long getId();

	void setId(Long id);

	String getName();

	void setName(String name);

	String getAddress();

	void setAddress(String address);

	List<User> getEmployees();

	void setEmployees(List<User> employees);

	List<User> getSchoolCoordinators();

	void setSchoolCoordinators(List<User> schoolCoordinators);

}