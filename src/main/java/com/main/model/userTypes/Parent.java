package com.main.model.userTypes;
import java.util.Arrays;
import java.util.Collection;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.
        SimpleGrantedAuthority;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Parent extends User {
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "PhoneNumber is mandatory")
    private String phoneNumber;
    private boolean enabled;

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

    public Parent(){
        super();
    }

    public Parent(String username, String password, String fullname, String email, String phoneNumber) {
        super(username, password, fullname);
        this.email = email;
        this.phoneNumber = phoneNumber;
        enabled = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_PARENT"));
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
