package com.main.assured;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.main.data.TestUserControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.repository.VerificationTokenRepository;

import io.restassured.http.ContentType;

public class AssuredExtendedUserControllerTest extends AbstractAssuredTest {

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
	public void passwordResetTest() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("email", parent.getEmail());
		String json = mapToJson(map);
		String oldPassword = getPassword();

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
		String newPassword = getPassword();
		Assert.assertNotEquals(oldPassword, newPassword);

	}

	@Test
	public void passwordResetNegativeTest() throws Exception {

	}

	private String getPassword() {
		UserRepository userRepository = context.getBean(UserRepository.class);
		IUser user = userRepository.findByUsername(parent.getUsername());
		return user.getPassword();
	}
}
