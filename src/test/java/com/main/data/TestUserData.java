package com.main.data;

import com.main.dto.UserDTO;
import com.main.model.userTypes.User;
import com.main.model.userTypes.UserBuilder;
import com.main.model.userTypes.interfaces.*;

/**
 * Ein Enum welches UserDTOs enthält, welche zum Testen
 * genutzt werden können.
 * 
 * @author Markus
 * @since 16.10.2019
 * @version 1.0
 */

public enum TestUserData {
	TEST_TEACHER(UserBuilder.<ITeacher>next().withName("Max_Teacher").withFullname("Mustermann").withPassword("password")
			.withRole("TEACHER").withEmail("max.mustermann@gmail.com").withPhoneNumber("0153323123").toDto("TEACHER")),
	TEST_PARENT(UserBuilder.<IParent>next().withName("Max_Parent").withFullname("Mustermann").withPassword("password")
			.withRole("PARENT").withEmail("max.mustermann@gmail.com").withPhoneNumber("0153323123").toDto("PARENT")),
	TEST_USER(UserBuilder.<IUser>next().withName("Max_User").withFullname("Mustermann").withPassword("password")
			.withRole("USER").withEmail("max.mustermann@gmail.com").withPhoneNumber("0153323123").toDto("USER")),
	TEST_CHILD(UserBuilder.<IChild>next().withName("Max_Child").withFullname("Mustermann").withPassword("password")
			.withRole("CHILD").withEmail("max.mustermann@gmail.com").withSchoolClass("1a").withPhoneNumber("0153323123").toDto("CHILD"));
	
	
	public UserDTO getUserDTO() {
		return userDTO;
	}
	
	public User getUser() {
		return UserBuilder.<User>next().withDto(userDTO).build();
	}

	private UserDTO userDTO;

	private TestUserData(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

}
