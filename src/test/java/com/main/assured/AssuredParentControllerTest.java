package com.main.assured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.data.TestParentControllerPath;
import com.main.data.TestUserData;
import com.main.dto.ChildDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.School;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;

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

    private long id = 0L;

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

    /*

    @Test
    @Transactional
    public void createChild() throws JsonProcessingException {
        ChildDTO childDTO = new ChildDTO();
        childDTO.setUserType("CHILD");
        childDTO.setFullname("Patrick Star");
        childDTO.setSchoolClass("3b");
        childDTO.setSchool(school1.getId());

        ValidatableResponse response = super.sendPostWithAuthAndJSON(parentDTO, TestParentControllerPath.CREATE_CHILD.getUri(), mapToJson(childDTO));

        String body = response.extract().body().asString();
        System.out.println(body);

        assertEquals(201, response.extract().statusCode());

    }*/

    @Test
    public void getChildrenByParentUserName(){
        Response response = super.sendGetRequestWithAuth(TestUserData.TEST_PARENT_4.getUserDTO(), TestParentControllerPath.CHILDREN.getUri());

        String body = response.body().asString();
        System.out.println(body);
        assertEquals(200, response.statusCode());

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