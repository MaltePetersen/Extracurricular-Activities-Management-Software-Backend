package com.main.model;

import java.util.List;
import java.util.Set;

public interface IRole {

	long getId();

	void setId(long id);

	String getName();

	void setName(String name);

	List<User> getUsers();

	void setUsers(List<User> users);

	List<Privilege> getPrivileges();

	void setPrivileges(List<Privilege> privileges);

	void addUser(User user);

}