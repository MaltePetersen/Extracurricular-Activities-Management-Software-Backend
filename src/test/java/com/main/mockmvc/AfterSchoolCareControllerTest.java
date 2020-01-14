package com.main.mockmvc;

import static org.junit.Assert.assertEquals;

import com.main.data.TestAfterSchoolCareControllerPath;
import com.main.dto.AfterSchoolCareDTO;
import com.main.model.School;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.service.AfterSchoolCareService;
import com.main.service.SchoolService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import lombok.extern.java.Log;

@Log
public class AfterSchoolCareControllerTest extends AbstractMvcTest {
	private School testSchool;

	@Autowired
	private SchoolService schoolService;

	@Autowired
	private AfterSchoolCareService afterSchoolCareService;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        testSchool = new School("Test-Schule", "Musterstraße 123, 1111 Musterstadt");

		schoolService.save(testSchool);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testGetAfterSchoolCaresWithUserAuthority() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestAfterSchoolCareControllerPath.AFTER_SCHOOL_CARES.getUri())).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithAnonymousUser
    public void testGetAfterSchoolCaresWithoutAuthorities() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestAfterSchoolCareControllerPath.AFTER_SCHOOL_CARES.getUri())).andReturn();
        assertEquals(401, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    public void testPostAfterSchoolCareWithUserAuthority() throws Exception {
		AfternoonCare afternoonCare = new AfternoonCare();
		afternoonCare.setParticipatingSchool(testSchool);

		String inputJson = super.mapToJson(afternoonCare);

    	MvcResult mvcResult =
				mockMvc.perform(MockMvcRequestBuilders.post(TestAfterSchoolCareControllerPath.AFTER_SCHOOL_CARES.getUri()).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();

		int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);

		String content = mvcResult.getResponse().getContentAsString();
		AfterSchoolCareDTO resultAfterSchoolCare = objectMapper.readValue(content, AfterSchoolCareDTO.class);

		assertEquals(afternoonCare.getParticipatingSchool().getId(), resultAfterSchoolCare.getParticipatingSchool());
    }
}
