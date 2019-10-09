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
public class Employee extends User {

    private String email;
    private String phoneNumber;
    private String Studienfach;
    private String iban;
    private String Adresse;
    public Employee(){
        super();
    }

    public Employee(String username, String password, String fullname, String email, String phoneNumber, String studienfach, String iban, String adresse) {
        super(username, password, fullname);
        this.email = email;
        this.phoneNumber = phoneNumber;
        Studienfach = studienfach;
        this.iban = iban;
        Adresse = adresse;
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
