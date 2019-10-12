package com.main.model.userTypes;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Username is mandatory")
    private final String username;
    @NotBlank(message = "Password is mandatory")
    private final String password;
    @NotBlank(message = "Fullname is mandatory")
    private final String fullname;
    @NotBlank(message = "Role is mandatory")
    private String role;
    private String email;
    private String phoneNumber;
    private String address;
    private String subject;
    private String iban;
    private String schoolClass;
    private boolean isSchoolCoordinator;
    @Column(name = "enabled")
    private boolean enabled;
    //Constructur normal User
    public User(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        role = "USER";
        enabled = true;
    }

    //Constructor Child
    public User(String username, String password, String fullname, String schoolClass) {
        this(username, password, fullname);
        this.schoolClass = schoolClass;
        role = "CHILD";
        enabled = true;
    }

    //Constructer Parent
    public User(String username, String password, String fullname, String email, String phoneNumber) {
        this(username, password, fullname);
        this.email = email;
        this.phoneNumber = phoneNumber;
        role = "PARENT";
        enabled = false;
    }

    //Constructor Teacher and SchoolCoordinator
    public User(String username, String password, String fullname, String email, String phoneNumber, String subject, boolean isSchoolCoordinator) {
    this(username, password, fullname, email, phoneNumber);
        this.subject = subject;
        role = "TEACHER";
        this.isSchoolCoordinator = isSchoolCoordinator;
        enabled = false;
    }
    //Constructor Employee and SchoolCoordinator
    public User(String username, String password, String fullname, String email, String phoneNumber, String address,String subject, String iban, boolean isSchoolCoordinator) {
        this(username, password, fullname, email, phoneNumber, address);
        this.subject = subject;
        this.iban = iban;
        this.isSchoolCoordinator = isSchoolCoordinator;
        role = "EMPLOYEE";
        enabled = false;
    }

    //Constructor Management
    public User(String username, String password, String fullname, String email, String phoneNumber, String address) {
        this(username, password, fullname, email, phoneNumber);
        this.address = address;
        role = "MANAGEMENT";
    }




    public User() {
        username = null;
        password = null;
        fullname = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(isSchoolCoordinator)
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role), new SimpleGrantedAuthority("ROLE_SCHOOLCOORDINATOR"));
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}