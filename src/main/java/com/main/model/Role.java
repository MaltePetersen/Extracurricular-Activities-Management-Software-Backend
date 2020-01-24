package com.main.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class Role implements IRole {

	public Role(String name) {
		this();
		this.name = name;
	}

	public Role() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Version 1.0
	 */

	private static final long serialVersionUID = 1337L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	@ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
	private List<User> users = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private List<Privilege> privileges = new ArrayList<Privilege>();

	@Override
	public void addUser(User user) {
		users.add(user);
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}



}