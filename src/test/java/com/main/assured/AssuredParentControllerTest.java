package com.main.assured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.data.TestParentControllerPath;
import com.main.data.TestUserData;
import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.ChildDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Attendance;
import com.main.model.Role;
import com.main.model.School;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
    private User child6;
    private AfternoonCare testAfternoonCare;
    private AfternoonCare testAfternoonCareWithDifferentParent;

    private IUserDTO parentDTO = TestUserData.TEST_PARENT_7.getUserDTO();
    private IUserDTO child6DTO = TestUserData.TEST_CHILD_6.getUserDTO();

    private long id = 0L;

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(parentDTO.getUsername());
        if(myUser == null) {
            User parent = (User) User.UserBuilder.next().withDto(this.parentDTO).build();
            parent.setPassword(encoder.encode(parent.getPassword()));
            Role parentRole = roleRepository.findByName(UserRole.ROLE_PARENT.toString());
            parent = userService.update(parent, UserRole.ROLE_PARENT);

            child6 = (User) userService.findByUsername(child6DTO.getUsername());
            if(child6 == null) {
                child6 = (User) User.UserBuilder.next().withDto(this.child6DTO).build();
                child6.setPassword(encoder.encode(child6.getPassword()));
                child6.setParent(parent);
                Role childRole = roleRepository.findByName(UserRole.ROLE_CHILD.toString());
                userService.update(child6, UserRole.ROLE_CHILD);

                parent.addChild(child6);
                userService.update(parent);
            }
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
        child1.setParent(parent1);

        parent2.addChild(child2);
        child2.setParent(parent2);

        parent2.addChild(child3);
        child3.setParent(parent2);

        parent2.addChild(child4);
        child4.setParent(parent2);

        testAfternoonCare = new AfternoonCare();
        testAfternoonCare.setName("Test-Nachmittagsbetreuung");
        testAfternoonCare.setParticipatingSchool(school1);
        afterSchoolCareService.save(testAfternoonCare);


        Attendance attendance1 = new Attendance();
        attendance1.setChild((User) userService.findByUsername(child6DTO.getUsername()));
        attendance1.setAfterSchoolCare(testAfternoonCare);
        attendanceService.save(attendance1);

        testAfternoonCare.addAttendance(attendance1);
        afterSchoolCareService.save(testAfternoonCare);

        testAfternoonCareWithDifferentParent = new AfternoonCare();
        testAfternoonCareWithDifferentParent.setName("Test-Nachmittagsbetreuung");
        testAfternoonCareWithDifferentParent.setParticipatingSchool(school1);
        afterSchoolCareService.save(testAfternoonCareWithDifferentParent);
    }

    /*
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

        assertEquals(201, response.extract().statusCode());

    }*/

    @Test
    public void getChildrenByParentUserName(){
        Response response = super.sendGetRequestWithAuth(TestUserData.TEST_PARENT_7.getUserDTO(), TestParentControllerPath.CHILDREN.getUri());

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

    @Test
    public void testGetAfternoonCaresWithParentAuthority() {
        ValidatableResponse response = super.sendGetRequestWithAuth(parentDTO, TestParentControllerPath.AFTER_SCHOOL_CARES.getUri()).then().assertThat().statusCode(200);

        List<AfterSchoolCareDTO> resultAfternoonCares = Arrays.asList(response.extract().body().as(AfterSchoolCareDTO[].class));

        assertTrue(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCare.getId())));
        assertTrue(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCareWithDifferentParent.getId())));
    }

    @Test
    public void testGetBookedAfternoonCaresWithParentAuthority() {
        ValidatableResponse response = super.sendGetRequestWithAuth(parentDTO, TestParentControllerPath.BOOKED_AFTER_SCHOOL_CARES.getUri()).then().assertThat().statusCode(200);

        List<AfterSchoolCareDTO> resultAfternoonCares = Arrays.asList(response.extract().body().as(AfterSchoolCareDTO[].class));

        assertTrue(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCare.getId())));
        assertFalse(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCareWithDifferentParent.getId())));
    }

    @After
    @Transactional
    public void teardown() {
        userService.deleteByName(child6DTO.getUsername());
        userService.deleteByName(parentDTO.getUsername());
    }
}