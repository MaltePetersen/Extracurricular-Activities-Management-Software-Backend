package com.main.dto;

import com.main.dto.interfaces.IUserDTO;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ChildDTO {
    @NotBlank(message = "UserType is mandatory")
    private String userType;

    private String username;
    private String password;
    private String email;
    @NotBlank(message = "Fullname is mandatory")
    private String fullname;
    @NotBlank(message = "Fullname is mandatory")
    private String schoolClass;
    @NotBlank(message = "Fullname is mandatory")
    private Long childSchool;

    public IUserDTO toUserDTO(String username, String password, String email){
        IUserDTO dto = UserDTO.builder().build();
        dto.setUserType(userType);
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setEmail(email);
        dto.setFullname(fullname);
        dto.setSchoolClass(schoolClass);
        dto.setChildSchool(childSchool);
        return dto;
    }
}