package com.main.mockmvc;

import com.main.data.TestEmployeeControllerPath;
import com.main.model.Attendance;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.repository.UserRepository;
import com.main.service.implementations.AfterSchoolCareService;
import com.main.service.implementations.AttendanceService;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@Log
public class EmployeeControllerTest extends AbstractMvcTest {
	private AfternoonCare testAfternoonCare;
	private AfternoonCare testAfternoonCareWithDifferentUser;

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

		Attendance attendance = new Attendance();
		attendance.setNote("Darf früher gehen");
		attendance.setArrivalTime(LocalDateTime.of(2020, 4, 3, 12, 2));
		attendance.setAfterSchoolCare(testAfternoonCare);
		attendance.setChild(userRepository.findByUsername("Child_Test"));
		attendanceService.save(attendance);

		testAfternoonCare.addAttendance(attendance);
		afterSchoolCareService.save(testAfternoonCare);

		testAfternoonCareWithDifferentUser = new AfternoonCare();
		testAfternoonCareWithDifferentUser.setName("Test-Nachmittagsbetreuung (2)");
		testAfternoonCareWithDifferentUser.setOwner(userRepository.findByUsername("Employee_Test2"));
		afterSchoolCareService.save(testAfternoonCareWithDifferentUser);

		super.setUp();
	}

	@Test
	@WithAnonymousUser
	public void testGetAfternoonCaresWithoutAuthorities() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TestEmployeeControllerPath.AFTER_SCHOOL_CARES.getUri())).andReturn();
		assertEquals(401, mvcResult.getResponse().getStatus());
	}
}
