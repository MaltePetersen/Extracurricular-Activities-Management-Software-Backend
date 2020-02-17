package com.main.data;

import com.main.dto.interfaces.IUserDTO;
import com.main.model.User;
import com.main.model.interfaces.*;

/**
 * Ein Enum welches UserDTOs enthält, welche zum Testen genutzt werden können.
 *
 * @author Markus
 * @version 1.0
 * @since 16.10.2019
 */

public enum TestUserData {
    TEST_MANAGEMENT(User.UserBuilder.<ITeacher>next().withName("Max_Management").withFullname("Mustermann")
            .withPassword("password").withEmail("max.management@gmail.com").withPhoneNumber("0153323123")
            .toDto("MANAGEMENT")),
    TEST_EMPLOYEE(User.UserBuilder.<ITeacher>next().withName("Max_Employee").withFullname("Mustermann")
            .withPassword("password").withEmail("max.employee@gmail.com").withPhoneNumber("0153323123")
            .toDto("EMPLOYEE")),
    TEST_TEACHER(User.UserBuilder.<ITeacher>next().withName("Max_Teacher").withFullname("Mustermann")
            .withPassword("password").withEmail("max.teacher@gmail.com").withPhoneNumber("0153323123")
            .toDto("TEACHER")),
    TEST_SCHOOL_COORDINATOR(User.UserBuilder.<ISchoolCoordinator>next().withName("Max_SCHOOL_COORDINATOR").withFullname("Mustermann")
			.isSchoolCoordinator(true)
            .withPassword("password").withEmail("max.teacher@gmail.com").withPhoneNumber("0153323123")
            .toDto("SCHOOL_COORDINATOR")),
    TEST_PARENT(User.UserBuilder.<IParent>next().withName("Max_Parent").withFullname("Mustermann")
            .withPassword("password").withEmail("max.parent@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
    TEST_PARENT_2(User.UserBuilder.<IParent>next().withName("Max_Parent2").withFullname("Mustermann")
            .withPassword("password").withEmail("max.parent2@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
    TEST_PARENT_3(User.UserBuilder.<IParent>next().withName("Max_Parent3").withFullname("Mustermann")
            .withPassword("password").withEmail("max.parent3@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
    TEST_PARENT_4(User.UserBuilder.<IParent>next().withName("Parent_Test").withFullname("Mustermann")
            .withPassword("password").withEmail("max.parent3@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
    TEST_PARENT_5(User.UserBuilder.<IParent>next().withName("Parent_Test2").withFullname("Mustermann")
            .withPassword("password").withEmail("max.parent3@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
    TEST_PARENT_6(User.UserBuilder.<IParent>next().withName("Parent_Test3").withFullname("Mustermann")
            .withPassword("password").withEmail("max.parent3@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
    TEST_PARENT_7(User.UserBuilder.<IParent>next().withName("Max_Parent_Test7").withFullname("Mustermann")
            .withPassword("Password123").withEmail("max.parent11@gmx.de").withPhoneNumber("0153323123").toDto("PARENT")),
    TEST_USER(User.UserBuilder.<IUser>next().withName("Max_User").withFullname("Mustermann").withPassword("password")
            .withEmail("max.user@gmail.com").withPhoneNumber("0153323123").toDto("USER")),
    TEST_CHILD(User.UserBuilder.<IChild>next().withName("Max_Child").withFullname("Mustermann").withPassword("password")
            .withEmail("max.child@gmail.com").withSchoolClass("1a").withPhoneNumber("0153323123").toDto("CHILD")),
    TEST_CHILD_2(User.UserBuilder.<IChild>next().withName("Child_Test2").withFullname("Mustermann")
            .withPassword("password").withEmail("max.child2@gmail.com").withSchoolClass("1a")
            .withPhoneNumber("0153323123").toDto("CHILD")),
    TEST_CHILD_3(User.UserBuilder.<IChild>next().withName("Child_Test3").withFullname("Mustermann")
            .withPassword("password").withEmail("max.child2@gmail.com").withSchoolClass("3a")
            .withPhoneNumber("0153323123").toDto("CHILD")),
    TEST_CHILD_4(User.UserBuilder.<IChild>next().withName("Child_Test4").withFullname("Mustermann")
            .withPassword("password").withEmail("max.child2@gmail.com").withSchoolClass("4a")
            .withPhoneNumber("0153323123").toDto("CHILD")),
    TEST_CHILD_5(User.UserBuilder.<IChild>next().withName("Child_Test5").withFullname("Mustermann")
            .withPassword("password").withEmail("max.child2@gmail.com").withSchoolClass("1a")
            .withPhoneNumber("0153323123").toDto("CHILD")),
    TEST_CHILD_6(User.UserBuilder.<IChild>next().withName("Max_Child_Test6").withFullname("Mustermann 6")
            .withPassword("password").withEmail("max.child2@gmail.com").withSchoolClass("1a")
            .withPhoneNumber("0153323123").toDto("CHILD"));

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
