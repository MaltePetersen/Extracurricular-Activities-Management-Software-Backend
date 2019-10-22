package com.main.model;

import com.main.model.userTypes.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@Entity
public class School {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "school name is mandatory")
    private String name;

    @NotBlank(message = "school address is mandatory")
    private String address;
    private User contact;
    private String email;
    private String phoneNumber;

    private ArrayList<User> schoolCoordinators;

    public School() {
        name = null;
        address = null;
    }

    public School(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public School(String name, String address, String email, String phoneNumber) {
        this(name, address);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
