package com.main.mockmvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.main.data.TestUserControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.interfaces.IContactDetails;
import com.main.model.interfaces.IUser;
import com.main.model.interfaces.IVerificationToken;
import com.main.repository.VerificationTokenRepository;
import com.main.service.UserService;

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
		String uri = TestUserControllerPath.REGISTER.getUri();
		IUserDTO teacher = TestUserData.TEST_TEACHER.getUserDTO();

		String inputJson = super.mapToJson(teacher);
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
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

		IUserDTO parent = TestUserData.TEST_PARENT_3.getUserDTO();

		String inputJson = super.mapToJson(parent);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(TestUserControllerPath.REGISTER.getUri())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		log.info(mvcResult.getResponse().getContentAsString());
		assertEquals(201, status);
		IUser user = userService.findByEmail(parent.getEmail());
		assertNotEquals(null, user);
		assertEquals(false, user.isVerified());

		IVerificationToken token = verificationTokenRepo.findByUser_Email(((IContactDetails) user).getEmail());
		mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get(TestUserControllerPath.EMAILCONFIRMATION.getUri() + token.getToken()))
				.andReturn();
		assertEquals(202, mvcResult.getResponse().getStatus());

		user = userService.findByEmail(parent.getEmail());
		assertEquals(true, user.isVerified());
		
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void createChildTest() throws Exception {
		String uri = TestUserControllerPath.REGISTER.getUri();
		IUserDTO child = TestUserData.TEST_CHILD.getUserDTO();

		String inputJson = super.mapToJson(child);
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
	}

}
