package com.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.interfaces.IChild;
import com.main.model.interfaces.IContactDetails;
import com.main.model.interfaces.IEmployee;
import com.main.model.interfaces.ISchool;
import com.main.model.interfaces.ISchoolCoordinator;

import lombok.Data;

@Entity
@Data
public class School implements IContactDetails, ISchool {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "school name is mandatory")
	private String name;

	@NotBlank(message = "school address is mandatory")
	private String address;

	private String email;
	private String phoneNumber;

	@ManyToMany(mappedBy = "employeesSchools")
	private List<User> employees = new ArrayList<>();

	@ManyToMany(mappedBy = "schoolCoordinatorsSchools")
	private List<User> schoolCoordinators = new ArrayList<>();

	@OneToMany(mappedBy = "childSchool", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> children = new ArrayList<>();

	@OneToOne(mappedBy = "participatingSchool")
	private AfterSchoolCare afterSchoolCare;
	
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
	
	public boolean removeChild(IChild child) {
		int oldSize = children.size();
		schoolCoordinators = schoolCoordinators.stream().filter(each -> each.getId() != child.getId())
				.collect(Collectors.toList());
		return oldSize > schoolCoordinators.size();
	}
	
	public void addChild(IChild child) {
		children.add((User) child);
	}

}
