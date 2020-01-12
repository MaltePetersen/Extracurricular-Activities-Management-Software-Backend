package com.main.mockmvc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.model.School;
import com.main.model.interfaces.ISchool;

import lombok.extern.java.Log;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Log
public class ParentControllerTest extends AbstractMvcTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void testGetParentWithAuthorityParent() throws Exception {
		String uri = "/api/parent/authority";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(202, mvcResult.getResponse().getStatus());
	}

	@Test
	@WithAnonymousUser
	public void testGetParentWithoutAuthorities() throws Exception {
		String uri = "/api/parent/authority";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(401, mvcResult.getResponse().getStatus());
	}
}
