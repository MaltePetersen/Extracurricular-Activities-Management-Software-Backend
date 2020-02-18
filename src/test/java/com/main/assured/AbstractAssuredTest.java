package com.main.assured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.data.TestUserControllerPath;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import com.main.repository.RoleRepository;
import com.main.repository.VerificationTokenRepository;
import com.main.service.implementations.AfterSchoolCareService;
import com.main.service.implementations.AttendanceService;
import com.main.service.implementations.SchoolService;
import com.main.service.implementations.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hibernate.usertype.UserType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.*;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractAssuredTest {

    @LocalServerPort
    private int port;

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

    @Autowired
    protected AttendanceService attendanceService;

    @Autowired
    protected SchoolService schoolService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepo;

    protected ObjectMapper objectMapper;

    public void setUp() throws Exception {
        RestAssured.port = port;
        RestAssuredMockMvc.webAppContextSetup(context);
        objectMapper = new ObjectMapper();
    }

    @Transactional
    protected User registerUser(IUserDTO userDTO) throws JsonProcessingException {
        userDTO.setPassword("Password123");

        UserRole oldRole = null;

        if(!userDTO.getUserType().equals("PARENT")){
            oldRole = UserRole.valueOf( userDTO.getUserType() );
        }


        userDTO.setEmail("example" + new Random().nextInt(100) + "@gmx.de");
        userDTO.setUserType("PARENT");

        String value = mapToJson(userDTO);

        IUser user = userService.findByUsername(userDTO.getUsername());
        if (user == null) {
            given().contentType("application/json").body(value).when().post(TestUserControllerPath.REGISTER.getUri()).then()
                    .assertThat().statusCode(201);

            String token = verificationTokenRepo.findByUser_Email(userDTO.getEmail()).getToken();

            given().with().auth().preemptive().basic(userDTO.getUsername(), userDTO.getPassword()).log().headers().when()
                    .get(TestUserControllerPath.EMAILCONFIRMATION.getUri() + token).then().assertThat().statusCode(202);
        }


        user = userService.findByUsername(userDTO.getUsername());
        if(oldRole != null){
            if(oldRole != UserRole.ROLE_PARENT){
                Role role = roleRepository.findByName(oldRole.toString());
                List<Role> roles = Arrays.asList(role);
                user.setRoles(roles);
                userService.update((User) user);
            }
            userDTO.setUserType(oldRole.toString());
        }

        return (User) user;
    }

    protected void deleteUser(IUserDTO userDTO) {
        IUser user = userService.findByUsername(userDTO.getUsername());
        if (user != null) {
            userService.deleteByName(userDTO.getUsername());
        }

    }


    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected Response sendGetRequestWithAuth(IUserDTO user, String uri) {
        return sendGetRequestWithAuthAndParams(user, uri, new HashMap<>());
    }

    protected Response sendGetRequestWithAuthAndParams(IUserDTO user, String uri, HashMap<String, Object> params) {
        return given().contentType(ContentType.JSON).with()
                .auth().preemptive().basic(user.getUsername(), user.getPassword())
                .when().params(params).log().all()
                .get(uri);
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

    protected ValidatableResponse sendPatchWithAuthAndUserNameAndJSON(IUserDTO user, String uri, String json) {
        return given().contentType(ContentType.JSON).with()
                .auth().preemptive().basic(user.getUsername(), user.getPassword())
                .body(json)
                .when().log().all()
                .patch(uri).then().assertThat();
    }


}
