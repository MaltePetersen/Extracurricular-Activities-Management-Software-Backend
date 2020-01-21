package com.main.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SimpleUserDTO {
    @NotBlank(message = "UserType is mandatory")
    private String userType;
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "Fullname is mandatory")
    private String fullname;
    private String schoolClass;
}