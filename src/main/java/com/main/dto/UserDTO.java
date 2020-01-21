package com.main.dto;

import com.main.dto.interfaces.IUserDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDTO implements IUserDTO {
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
    private Long childSchool;

	private String phoneNumber;
    private String subject;
    private String iban;
    private String address;
    private boolean isSchoolCoordinator;


}
