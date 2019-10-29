package com.main.assured;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.main.data.TestUserControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.repository.VerificationTokenRepository;

import io.restassured.http.ContentType;

public class AssuredExtendedRegisterationController extends AbstractAssuredTest {

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

//	@Test
//	public void createChildTest() throws Exception {
//		IUserDTO child = TestUserData.TEST_CHILD_2.getUserDTO();
//
//		String value = mapToJson(child);
//		given().contentType("application/json").with().auth().preemptive()
//				.basic(parent.getUsername(), parent.getPassword()).body(value).log().headers().when()
//				.post(TestUserControllerPath.REGISTER.getUri()).then().assertThat().statusCode(201);
//	}

	@Test
	public void passwordResetTest() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("email", parent.getEmail());
		String json = mapToJson(map);

		// Login-Test
		given().contentType(ContentType.JSON).with().auth().preemptive()
				.basic(parent.getUsername(), parent.getPassword()).when().get("/login").then().assertThat()
				.statusCode(200);
		// Reset-Password-Test
		given().contentType(ContentType.JSON).with().auth().preemptive()
				.basic(parent.getUsername(), parent.getPassword()).body(json).log().headers().when()
				.post(TestUserControllerPath.RESETPASSWORD.getUri()).then().assertThat().statusCode(202);
		// Login-Test
		given().contentType(ContentType.JSON).with().auth().preemptive()
				.basic(parent.getUsername(), parent.getPassword()).when().get("/login").then().assertThat()
				.statusCode(401);
	}

}
