package com.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.main.model.interfaces.ISchool;
import com.main.model.userTypes.User;
import com.main.model.userTypes.interfaces.IContactDetails;
import com.main.model.userTypes.interfaces.IEmployee;
import com.main.model.userTypes.interfaces.ISchoolCoordinator;

import lombok.Data;

@Entity
@Data
public class School implements IContactDetails, ISchool {

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank(message = "school name is mandatory")
	private String name;

	@NotBlank(message = "school address is mandatory")
	private String address;

	private String email;
	private String phoneNumber;

	@ManyToMany(mappedBy = "employeesSchools")
	private List<User> employees = new ArrayList<User>();

	@ManyToMany(mappedBy = "schoolCoordinatorsSchools")
	private List<User> schoolCoordinators = new ArrayList<>();

	@OneToMany(mappedBy = "childSchool", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> children = new ArrayList<>();

	public School() {
		name = null;
		address = null;
	}

	public School(String name, String address) {
		this();
		this.name = name;
		this.address = address;
	}

	public School(String name, String address, String email, String phoneNumber) {
		this(name, address);
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public boolean removeEmployee(IEmployee employee) {
		int oldSize = employees.size();
		employees = employees.stream().filter(each -> each.getId() != employee.getId()).collect(Collectors.toList());
		return oldSize > employees.size();
	}

	public void addEmployee(IEmployee employee) {
		employees.add((User) employee);
	}

	public boolean removeSchoolCoordinator(ISchoolCoordinator schoolCoordinator) {
		int oldSize = schoolCoordinators.size();
		schoolCoordinators = schoolCoordinators.stream().filter(each -> each.getId() != schoolCoordinator.getId())
				.collect(Collectors.toList());
		return oldSize > schoolCoordinators.size();
	}

	public void addSchoolCoordinator(ISchoolCoordinator schoolCoordinator) {
		schoolCoordinators.add((User) schoolCoordinator);
	}

}
