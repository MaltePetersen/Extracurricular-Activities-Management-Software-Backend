package com.main.assured;

import com.main.data.TestEmployeeControllerPath;
import com.main.data.TestParentControllerPath;
import com.main.data.TestUserData;
import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Attendance;
import com.main.model.Role;
import com.main.model.School;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AssuredParentControllerTest extends AbstractAssuredTest {

    private School school1;
    private School school2;
    private School school3;
    private User parent1;
    private User parent2;
    private User parent3;
    private User child1;
    private User child2;
    private User child3;
    private User child4;
    private User child5;

    private IUserDTO parentDTO = TestUserData.TEST_PARENT_4.getUserDTO();

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(parentDTO.getUsername());
        if(myUser == null) {
            User user = (User) User.UserBuilder.next().withDto(this.parentDTO).build();
            user.setPassword(encoder.encode(user.getPassword()));
            Role role = roleRepository.findByName(UserRole.ROLE_PARENT.toString());
            user = userService.update(user, UserRole.ROLE_PARENT);
        }

        //create test data
        parent1 = TestUserData.TEST_PARENT_4.getUser();
        parent2 = TestUserData.TEST_PARENT_5.getUser();
        parent3 = TestUserData.TEST_PARENT_6.getUser();
        child1 = TestUserData.TEST_CHILD.getUser();
        child2 = TestUserData.TEST_CHILD_2.getUser();
        child3 = TestUserData.TEST_CHILD_3.getUser();
        child4 = TestUserData.TEST_CHILD_4.getUser();
        child5 = TestUserData.TEST_CHILD_5.getUser();


        school1 = new School("Test-Schule 1", "Test-Adresse 1");
        schoolService.save(school1);
        school2 = new School("Test-Schule 2", "Test-Adresse 2");
        schoolService.save(school2);
        school3 = new School("Test-Schule 3", "Test-Adresse 3");
        schoolService.save(school3);

        parent1.addChild(child1);
        parent2.addChild(child2);
        parent2.addChild(child3);
        parent2.addChild(child4);

    }



    @Test
    public void createChild(){
        ValidatableResponse response = super.sendPostWithAuthAndJSON(parentDTO, TestParentControllerPath.CREATE_CHILD.getUri(), "\t{\n" +
                "\t\"userType\": \"CHILD\",\n" +
                "    \"fullname\": \"Patrick Star\",\n" +
                "    \"schoolClass\": \"3b\",\n" +
                "\t\"school\": \""+ school1.getId() + "\"\n" +
                "\t}\n" +
                "\n" +
                "\t");

        User.UserBuilder builder = User.UserBuilder.next();
        User child = (User) builder.withDto(response.extract().body().as(UserDTO.class)).build();
        assertEquals(201, response.extract().statusCode());
        assertEquals(parentDTO.getUsername(), ((User) userService.findByUsername(child.getUsername())).getParent().getUsername());


    }

    @Test
    public void getChildrenByParentUserName(){
        ValidatableResponse response = super.sendGetRequestWithAuth(TestUserData.TEST_PARENT_4.getUserDTO(), TestParentControllerPath.CHILDREN.getUri());

        assertEquals(200, response.extract().statusCode());

    }

   /* @Test
    public void updateChildFullname(){

        String newFullname = "Ben Müller";

        ValidatableResponse response = super.sendPatchWithAuthAndUserNameAndJSON(TestUserData.TEST_PARENT_2.getUserDTO(),
                TestParentControllerPath.UPDATE_CHILD.getUri() + TestUserData.TEST_CHILD_3.getUserDTO().getUsername(),
                "\t{\n" +
                "\t\"fullname\": \"" + newFullname + "\"\n" +
                "\t}\n");

        IUserDTO dto = response.extract().body().as(IUserDTO.class);

        assertEquals(newFullname, dto.getFullname());
    }*/
}