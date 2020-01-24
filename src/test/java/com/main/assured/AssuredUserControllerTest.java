package com.main.assured;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.model.interfaces.IUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.main.data.TestUserControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;

import lombok.extern.java.Log;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Log
public class AssuredUserControllerTest extends AbstractAssuredTest {

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}



	@Test
	public void registerNewParentTest() throws Exception {


		IUserDTO parent = TestUserData.TEST_PARENT_2.getUserDTO();

		String value = mapToJson(parent);

		given().contentType("application/json").body(value).when().post(TestUserControllerPath.REGISTER.getUri())
				.then().assertThat().statusCode(201);
		
		given().auth().preemptive().basic(parent.getUsername(), parent.getPassword()).log().headers().when()
				.get(TestUserControllerPath.RESENDTOKEN.getUri()).then().statusCode(202);
	}



	
	

}
