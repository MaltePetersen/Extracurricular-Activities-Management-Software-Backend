package com.main.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "Username is mandatory")
	private String username;

    @NotBlank(message = "Password is mandatory")
	private String password;

	@Email
    @NotBlank(message = "Email is mandatory")
	private String email;

    @NotBlank(message = "Fullname is mandatory")
    private String fullname;

}
