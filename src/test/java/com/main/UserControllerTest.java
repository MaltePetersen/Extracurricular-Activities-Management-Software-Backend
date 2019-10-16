package com.main;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.main.data.TestUserData;
import com.main.dto.UserDTO;

/**
 * Klasse zum Testen der Funktionalitäten des
 * UserControllers.
 * 
 * @author Markus
 * @version 1.0
 * @since 16.10.2019
 * 
 */

public class UserControllerTest extends  AbstractMvcTest{
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	@Test
	@WithMockUser(authorities = "ROLE_MANAGEMENT")
	public void createTeacherTest() throws Exception {
		String uri = "/register";
		UserDTO teacher = TestUserData.TEST_TEACHER.getUserDTO();
		
		String inputJson = super.mapToJson(teacher);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson))
				 .andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
	    assertEquals("Created [ROLE_TEACHER]", content);
	}
	
	
	@Test
	@WithAnonymousUser
	public void createParentTest() throws Exception {
		String uri = "/register";
		UserDTO parent = TestUserData.TEST_PARENT.getUserDTO();
		
		String inputJson = super.mapToJson(parent);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson))
				 .andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
	    assertEquals("Created [ROLE_PARENT]", content);
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void createChildTest() throws Exception {
		String uri = "/register";
		UserDTO child = TestUserData.TEST_CHILD.getUserDTO();
		
		String inputJson = super.mapToJson(child);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
		         .contentType(MediaType.APPLICATION_JSON_VALUE)
		         .content(inputJson))
				 .andReturn();
		int status = mvcResult.getResponse().getStatus();
		System.out.println(mvcResult.getResponse().getContentAsString());
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
	    assertEquals("Created [ROLE_CHILD]", content);
	}

	
	
}
