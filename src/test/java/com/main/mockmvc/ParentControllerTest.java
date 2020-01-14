package com.main.mockmvc;

import com.main.data.TestParentControllerPath;
import com.main.dto.AfterSchoolCareDTO;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.service.AfterSchoolCareService;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@Log
public class ParentControllerTest extends AbstractMvcTest {
	private AfternoonCare testAfternoonCare;

	@Autowired
	private AfterSchoolCareService afterSchoolCareService;

	@Override
	@Before
	public void setUp() {
		testAfternoonCare = new AfternoonCare();
		testAfternoonCare.setName("Test-Nachmittagsbetreuung");
		afterSchoolCareService.save(testAfternoonCare);

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

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void testGetAfternoonCaresWithParentAuthority() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestParentControllerPath.AFTER_SCHOOL_CARES.getUri())).andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());
	}

	@Test
	@WithAnonymousUser
	public void testGetAfternoonCaresWithoutAuthorities() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestParentControllerPath.AFTER_SCHOOL_CARES.getUri())).andReturn();
		assertEquals(401, mvcResult.getResponse().getStatus());
	}

	@Test
	@WithMockUser(authorities = "ROLE_PARENT")
	public void testGetSingleAfternoonCareWithParentAuthority() throws Exception {
		String uri = TestParentControllerPath.AFTER_SCHOOL_CARES.getUri() + "/" + testAfternoonCare.getId();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

		assertEquals(200, mvcResult.getResponse().getStatus());

		String content = mvcResult.getResponse().getContentAsString();
		AfterSchoolCareDTO resultAfternoonCare = objectMapper.readValue(content, AfterSchoolCareDTO.class);

		assertEquals(testAfternoonCare.getId(), resultAfternoonCare.getId());
		assertEquals("Test-Nachmittagsbetreuung", resultAfternoonCare.getName());
	}

	@Test
	@WithAnonymousUser
	public void testGetSingleAfternoonCareWithoutAuthorities() throws Exception {
		String uri = TestParentControllerPath.AFTER_SCHOOL_CARES.getUri() + "/" + testAfternoonCare.getId();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(401, mvcResult.getResponse().getStatus());
	}
}
