package com.main.mockmvc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.junit.Assert.assertEquals;

public class SchoolControllerTest extends AbstractMvcTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void testGestSchoolsIfAuthorityIsParent() throws Exception{
		String uri = "/api/schools";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	@WithAnonymousUser
	public void testGestSchoolsIfAuthorityIsAnonymus() throws Exception{
		String uri = "/api/schools";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(401, mvcResult.getResponse().getStatus());
	}

}
