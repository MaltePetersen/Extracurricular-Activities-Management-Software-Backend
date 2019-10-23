package com.main.assured;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.data.TestUserControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.repository.VerificationTokenRepository;
import com.main.service.UserService;

public class AssuredExtendedRegisterationController extends AbstractAssuredTest {

	@Autowired
	private UserService userService;

	@Autowired
	private VerificationTokenRepository verificationTokenRepo;

	protected IUserDTO parent;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		parent = TestUserData.TEST_PARENT.getUserDTO();

		String value = mapToJson(parent);

		given().contentType("application/json").body(value).when().post(TestUserControllerPath.REGISTER.getUri()).then()
				.assertThat().statusCode(201);

		String token = verificationTokenRepo.findByUser_Email(parent.getEmail()).getToken();

		given().with().auth().preemptive().basic(parent.getUsername(), parent.getPassword()).log().headers().when()
				.get(TestUserControllerPath.EMAILCONFIRMATION.getUri() + token).then().assertThat().statusCode(202);
	}

	@Test
	public void createChildTest() throws Exception {
		IUserDTO child = TestUserData.TEST_CHILD_2.getUserDTO();

		String value = mapToJson(child);
		given().contentType("application/json").with().auth().preemptive().basic(parent.getUsername(), parent.getPassword()).body(value).log().headers().when()
				.post(TestUserControllerPath.REGISTER.getUri()).then().assertThat().statusCode(201);
	}

}
