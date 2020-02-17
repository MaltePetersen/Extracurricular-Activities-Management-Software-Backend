package com.main.assured;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import com.main.repository.RoleRepository;
import com.main.repository.VerificationTokenRepository;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.main.data.TestUserControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Log
public class AssuredUserControllerTest extends AbstractAssuredTest {


    private IUserDTO parent = TestUserData.TEST_PARENT_7.getUserDTO();

    @Autowired
    private VerificationTokenRepository verificationTokenRepo;

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
        parent.setPassword("Password123");
        String value = mapToJson(parent);

        IUser user = userService.findByUsername(parent.getUsername());
        if (user == null) {
            given().contentType("application/json").body(value).when().post(TestUserControllerPath.REGISTER.getUri()).then()
                    .assertThat().statusCode(201);

            String token = verificationTokenRepo.findByUser_Email(parent.getEmail()).getToken();

            given().with().auth().preemptive().basic(parent.getUsername(), parent.getPassword()).log().headers().when()
                    .get(TestUserControllerPath.EMAILCONFIRMATION.getUri() + token).then().assertThat().statusCode(202);
        }
    }

    @After
    public void tearDown(){
        if(userService.findByUsername(parent.getUsername()) != null)
            userService.deleteByName(parent.getUsername());
    }



    @Test
    public void registerNewParentTest() throws Exception {


        IUserDTO parent = TestUserData.TEST_PARENT_2.getUserDTO();
        parent.setPassword("Password123");

        String value = mapToJson(parent);

        given().contentType("application/json").body(value).when().post(TestUserControllerPath.REGISTER.getUri())
                .then().assertThat().statusCode(201);

        given().auth().preemptive().basic(parent.getUsername(), parent.getPassword()).log().headers().when()
                .get(TestUserControllerPath.RESENDTOKEN.getUri()).then().statusCode(202);

    }


    @Test
    @Transactional
    public void showProfileTest() throws Exception {
        Response response = sendGetRequestWithAuth(parent, TestUserControllerPath.PROFILE.getUri());

        response.then().assertThat().statusCode(200);
    }


}
