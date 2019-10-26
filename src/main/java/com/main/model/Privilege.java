package com.main.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.main.model.interfaces.IPrivilege;

import lombok.Data;

@Entity
@Data
public class Privilege implements IPrivilege {

	public Privilege(String name) {
		this();
		this.name = name;
	}

	public Privilege() {
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

	@ManyToMany(mappedBy = "privileges")
	private List<Role> roles = new ArrayList<Role>();

	@Override
	public String toString() {
		return "Privilege [id=" + id + ", name=" + name + "]";
	}

}
