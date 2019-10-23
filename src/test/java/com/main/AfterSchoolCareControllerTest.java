package com.main;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.main.mockmvc.AbstractMvcTest;

import lombok.extern.java.Log;

@Log
public class AfterSchoolCareControllerTest extends AbstractMvcTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@WithMockUser(authorities = "ROLE_USER")
	public void testGetAfterSchoolCaresWithUserAuthority() throws Exception {
		String uri = "/api/after_school_cares";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());
	}

	@Test
	@WithAnonymousUser
	public void testGetAfterSchoolCaresWithoutAuthorities() throws Exception {
		String uri = "/api/after_school_cares";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(401, mvcResult.getResponse().getStatus());
	}
}
