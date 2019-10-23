package com.main.assured;

import static org.junit.Assert.assertEquals;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.userTypes.interfaces.IParent;

import io.restassured.response.ValidatableResponse;
import lombok.extern.java.Log;

@Log
public class AssuredUserController extends AbstractAssuredTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void test() throws Exception {
		String registerUri = "/register";
		String resetToken = "/auth";

		IUserDTO parent = TestUserData.TEST_PARENT.getUserDTO();

		String value = mapToJson(parent);

		ValidatableResponse response = given().contentType("application/json").body(value).when().post(registerUri)
				.then().assertThat().statusCode(201);
		
		log.info("Login: " + parent.getUsername() + " " + parent.getPassword());
		
		given().auth().preemptive().basic("Employee_Test", "passoword").when().get(resetToken).then()
				.statusCode(202);

	}

}
