package com.main.mockmvc;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class AfterSchoolCareControllerTest extends AbstractMvcTest {
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@WithMockUser(authorities = "ROLE_EMPLOYEE")
	public void basicTest() throws Exception {
		String allafterSchools = "/api/after_school_cares";
		String allSchools = "/api/schools";

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(allafterSchools)).andReturn();
		int status = result.getResponse().getStatus();
		String content = result.getResponse().getContentAsString();
		System.out.println(content);
		assertEquals(200, status);

		result = mockMvc.perform(MockMvcRequestBuilders.get(allSchools)).andReturn();
		status = result.getResponse().getStatus();
		content = result.getResponse().getContentAsString();
		System.out.println(content);
		assertEquals(200, status);

	}
}
