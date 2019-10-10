package com.main.model.userTypes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Collection;
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Employee extends User {
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "PhoneNumber is mandatory")
    private String phoneNumber;
    @NotBlank(message = "Subject is mandatory")
    private String subject;
    @NotBlank(message = "Iban is mandatory")
    private String iban;
    @NotBlank(message = "Address is mandatory")
    private String address;
    public Employee(){
        super();
    }

    public Employee(String username, String password, String fullname, String email, String phoneNumber, String subject, String iban, String address) {
        super(username, password, fullname);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.subject = subject;
        this.iban = iban;
        this.address = address;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
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
        return true;
    }
}
