package com.main.model.userTypes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Child extends User {

    private String schoolClass;

    public Child() {
    }

    public Child(String username, String password, String fullname, String schoolClass) {
        super(username, password, fullname);
        this.schoolClass = schoolClass;
    }

    public Child(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_CHILD"));
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
