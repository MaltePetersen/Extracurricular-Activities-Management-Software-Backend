package com.main.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

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

	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<>();

	@ManyToMany
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