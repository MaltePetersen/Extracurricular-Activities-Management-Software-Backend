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
public class SchoolCoordinator extends User {

    public SchoolCoordinator(String username, String password, String fullname) {
        super(username, password, fullname);
    }

    public SchoolCoordinator() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_SCHOOLCOORDINATOR"));
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
