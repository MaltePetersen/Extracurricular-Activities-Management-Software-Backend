package com.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
//import static io.restassured.RestAssured.*;
//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.interfaces.IVerificationToken;
import com.main.model.userTypes.User;
import com.main.repository.VerificationTokenRepository;
import com.main.service.UserService;

import io.restassured.response.Response;
import lombok.extern.java.Log;

/**
 * Klasse zum Testen der Funktionalitäten des UserControllers.
 * 
 * @author Markus
 * @version 1.1
 * @since 16.10.2019, 19.10.2019
 * 
 */

@Log
public class UserControllerTest extends AbstractMvcTest {

	@Autowired
	private UserService userService;

	@Autowired
	private VerificationTokenRepository verificationTokenRepo;

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@WithMockUser(authorities = "ROLE_MANAGEMENT")
	public void createTeacherTest() throws Exception {
		String uri = "/register";
		IUserDTO teacher = TestUserData.TEST_TEACHER.getUserDTO();

		String inputJson = super.mapToJson(teacher);
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals("Created: TEACHER", content);
	}

	/**
	 * Testet das Registrieren und das Bestätigen per Email-Bestätigung
	 * 
	 * @category Testing
	 * @since 17.10.2019
	 * @author Markus
	 * @throws Exception
	 */

	@Test
	@WithAnonymousUser
	public void createParentAndEmailConfirmationTest() throws Exception {
		String registerUri = "/register";
		String confirmationUri = "/registrationConfirm?token=";
		String resendUri = "/resendToken";

		IUserDTO parent = TestUserData.TEST_PARENT.getUserDTO();

		String inputJson = super.mapToJson(parent);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(registerUri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		assertEquals("Created: PARENT", mvcResult.getResponse().getContentAsString());

//		Response response = given().auth().basic(parent.getUsername(), parent.getPassword()).when().get(resendUri).andReturn();
//		status = response.getStatusCode();
		
		User user = userService.findByEmail(parent.getEmail());
		assertNotEquals(null, user);
		assertEquals(false, user.isEnabled());

		IVerificationToken token = verificationTokenRepo.findByUser_Email(user.getEmail());
		mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(confirmationUri + token.getToken())).andReturn();
		assertEquals(202, mvcResult.getResponse().getStatus());

		user = userService.findByEmail(parent.getEmail());
		assertEquals(true, user.isEnabled());
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void createChildTest() throws Exception {
		String uri = "/register";
		IUserDTO child = TestUserData.TEST_CHILD.getUserDTO();

		String inputJson = super.mapToJson(child);
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		assertEquals("Created: CHILD", mvcResult.getResponse().getContentAsString());
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void resetTokenWithWrongAuthorityTest() throws Exception {
		String resendUri = "/resendToken";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(resendUri)).andReturn();
		assertEquals(403, mvcResult.getResponse().getStatus());
	}

	@Test
	@WithMockUser(authorities = "RESET_TOKEN")
	public void resetTokenWithRightAuthorityTest() throws Exception {
		String resendUri = "/resendToken";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(resendUri)).andReturn();
		assertEquals(202, mvcResult.getResponse().getStatus());
	}

}
