package com.main.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.main.utility.MemberRole;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    public Member() {
        this.enabled = false;
    }

    public Member(Set<MemberRole> roles,
                  @NotEmpty(message = "{username.notempty}") @Size(min = 6, max = 32, message = "{username.badformat}") String username,
                  @NotEmpty(message = "{password.notempty}") String password,
                  @Email @NotEmpty(message = "{email.notempty}") String email) {
        super();
        this.roles = roles;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "enabled")
    private boolean enabled;

    @ElementCollection(targetClass = MemberRole.class)
    @JoinTable(name = "memberRoles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role", nullable = true)
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>(Arrays.asList(MemberRole.ROLE_USER));

    private String username;

    private String password;

    private String email;


    private boolean agb;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return this.roles == null ? false : roles.contains(MemberRole.ROLE_ADMIN);
    }


}
