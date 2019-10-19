package com.main.data;



import com.main.dto.interfaces.IUserDTO;
import com.main.model.userTypes.User;
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
	TEST_TEACHER(User.UserBuilder.<ITeacher>next().withName("Max_Teacher").withFullname("Mustermann").withPassword("password")
			.withRole("TEACHER").withEmail("max.teacher@gmail.com").withPhoneNumber("0153323123").toDto("TEACHER")),
	TEST_PARENT(User.UserBuilder.<IParent>next().withName("Max_Parent").withFullname("Mustermann").withPassword("password")
			.withRole("PARENT").withEmail("max.parent@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
	TEST_USER(User.UserBuilder.<IUser>next().withName("Max_User").withFullname("Mustermann").withPassword("password")
			.withRole("USER").withEmail("max.user@gmail.com").withPhoneNumber("0153323123").toDto("USER")),
	TEST_CHILD(User.UserBuilder.<IChild>next().withName("Max_Child").withFullname("Mustermann").withPassword("password")
			.withRole("CHILD").withEmail("max.child@gmail.com").withSchoolClass("1a").withPhoneNumber("0153323123").toDto("CHILD"));
	
	
	public IUserDTO getUserDTO() {
		return userDTO;
	}
	
	public User getUser() {
		return User.UserBuilder.<User>next().withDto(userDTO).build();
	}

	private IUserDTO userDTO;

	private TestUserData(IUserDTO userDTO) {
		this.userDTO = userDTO;
	}

}
