package com.main.model;

import java.util.List;

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