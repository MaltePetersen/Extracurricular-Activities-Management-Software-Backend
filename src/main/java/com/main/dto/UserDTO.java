package com.main.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "UserType is mandatory")
    private String userType;
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Fullname is mandatory")
    private String fullname;
    private String schoolClass;
    private String phoneNumber;
    private String subject;
    private String iban;
    private String address;
    private boolean isSchoolCoordinator;

}
