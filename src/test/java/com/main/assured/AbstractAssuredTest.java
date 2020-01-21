package com.main.assured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.dto.interfaces.IUserDTO;
import com.main.repository.RoleRepository;
import com.main.service.AfterSchoolCareService;
import com.main.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.response.ValidatableResponse;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractAssuredTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    protected UserService userService;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
	protected PasswordEncoder encoder;

    @Autowired
    protected AfterSchoolCareService afterSchoolCareService;

    @LocalServerPort
    private int port;


    protected ObjectMapper objectMapper;

    public void setUp() throws Exception {
        RestAssured.port = port;
        RestAssuredMockMvc.webAppContextSetup(context);
        objectMapper = new ObjectMapper();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected ValidatableResponse sendGetRequestWithAuth(IUserDTO user, String uri) {
        return sendGetRequestWithAuthAndParams(user, uri, new HashMap<>());
    }

    protected ValidatableResponse sendGetRequestWithAuthAndParams(IUserDTO user, String uri, HashMap<String, Object> params) {
        return given().contentType(ContentType.JSON).with()
                .auth().preemptive().basic(user.getUsername(), user.getPassword())
                .when().params(params).log().all()
                .get(uri).then().assertThat();
    }

    protected ValidatableResponse sendPostWithAuth(IUserDTO user, String uri) {
        return sendPostWithAuthAndJSON(user, uri, "");
    }

    protected ValidatableResponse sendPostWithAuthAndJSON(IUserDTO user, String uri, String json) {
        return given().contentType(ContentType.JSON).with()
                .auth().preemptive().basic(user.getUsername(), user.getPassword())
                .body(json)
                .when().log().all()
                .post(uri).then().assertThat();
    }


}
