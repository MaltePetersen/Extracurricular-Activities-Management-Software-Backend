package com.main.mockmvc;

import static org.junit.Assert.assertEquals;

import com.main.data.TestSchoolControllerPath;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class SchoolControllerTest extends AbstractMvcTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void testGetSchoolsIfAuthorityIsParent() throws Exception{
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestSchoolControllerPath.SCHOOLS.getUri())).andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	@WithAnonymousUser
	public void testGetSchoolsIfAuthorityIsAnonymous() throws Exception{
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestSchoolControllerPath.SCHOOLS.getUri())).andReturn();
		assertEquals(401, mvcResult.getResponse().getStatus());
	}
}
