package com.main.mockmvc;

import com.main.data.TestParentControllerPath;
import com.main.dto.AfterSchoolCareDTO;
import com.main.model.afterSchoolCare.AfterSchoolCare;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.repository.UserRepository;
import com.main.service.implementations.AfterSchoolCareService;
import com.main.service.implementations.AttendanceService;
import lombok.extern.java.Log;
import org.json.JSONObject;
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

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Before
    public void setUp() {
        testAfternoonCare = new AfternoonCare();
        testAfternoonCare.setName("Test-Nachmittagsbetreuung");
        testAfternoonCare.setOwner(userRepository.findByUsername("Employee_Test"));
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
    public void testGetSchoolsWithParentAuthority() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestParentControllerPath.SCHOOLS.getUri())).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithAnonymousUser
    public void testGetSchoolsWithoutAuthorities() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestParentControllerPath.SCHOOLS.getUri())).andReturn();
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
        assertEquals(testAfternoonCare.getName(), resultAfternoonCare.getName());
        assertEquals(testAfternoonCare.getOwner().getUsername(), resultAfternoonCare.getOwner().getUsername());
    }

    @Test
    @WithAnonymousUser
    public void testGetSingleAfternoonCareWithoutAuthorities() throws Exception {
        String uri = TestParentControllerPath.AFTER_SCHOOL_CARES.getUri() + "/" + testAfternoonCare.getId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        assertEquals(401, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(authorities = "ROLE_PARENT")
    public void testGetAfterSchoolCareTypesWithParentAuthority() throws Exception {
        String uri = TestParentControllerPath.GET_TYPES.getUri();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        String content = mvcResult.getResponse().getContentAsString();
		JSONObject result = new JSONObject(content);

        assertEquals(result.get(Integer.toString(AfterSchoolCare.Type.AFTERNOON_CARE.getId())), AfterSchoolCare.getTypes().get(AfterSchoolCare.Type.AFTERNOON_CARE.getId()));
        assertEquals(result.get(Integer.toString(AfterSchoolCare.Type.WORKING_GROUP.getId())), AfterSchoolCare.getTypes().get(AfterSchoolCare.Type.WORKING_GROUP.getId()));
        assertEquals(result.get(Integer.toString(AfterSchoolCare.Type.REMEDIAL.getId())), AfterSchoolCare.getTypes().get(AfterSchoolCare.Type.REMEDIAL.getId()));
        assertEquals(result.get(Integer.toString(AfterSchoolCare.Type.AMPLIFICATION.getId())), AfterSchoolCare.getTypes().get(AfterSchoolCare.Type.AMPLIFICATION.getId()));
    }

	@Test
	@WithAnonymousUser
	public void testGetAfterSchoolCareTypesWithoutAuthorities() throws Exception {
		String uri = TestParentControllerPath.GET_TYPES.getUri();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();

		assertEquals(401, mvcResult.getResponse().getStatus());
	}
}
