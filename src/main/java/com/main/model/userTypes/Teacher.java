package com.main.model.userTypes;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
@Entity
@Data
public class Teacher extends User {

    private String email;
    private String phoneNumber;

    public Teacher(String username, String password, String fullname, String email, String phoneNumber) {
        super(username, password, fullname);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public Teacher(){

    }
    public Teacher(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_TEACHER"));
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
