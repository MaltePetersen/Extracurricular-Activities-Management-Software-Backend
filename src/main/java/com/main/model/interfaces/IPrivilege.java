package com.main.model.interfaces;

import java.util.List;

import com.main.model.Role;

public interface IPrivilege {

	long getId();

	void setId(long id);

	String getName();

	void setName(String name);

	List<Role> getRoles();

	void setRoles(List<Role> roles);

}