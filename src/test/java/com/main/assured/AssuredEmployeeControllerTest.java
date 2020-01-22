package com.main.assured;

import com.main.data.TestEmployeeControllerPath;
import com.main.data.TestUserData;
import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Attendance;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AssuredEmployeeControllerTest extends AbstractAssuredTest {
    private AfternoonCare testAfternoonCareWithUser;
    private AfternoonCare testAfternoonCareWithDifferentUser;

    private IUserDTO employee = TestUserData.TEST_EMPLOYEE.getUserDTO();

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(employee.getUsername());
        if(myUser != null)
            return;

        User user = (User) User.UserBuilder.next().withDto(this.employee).build();
        user.setPassword(encoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(UserRole.ROLE_EMPLOYEE.toString());
        user.getRoles().add(role);

        userService.update(user);

        testAfternoonCareWithUser = new AfternoonCare();
        testAfternoonCareWithUser.setName("Test-Nachmittagsbetreuung");
        testAfternoonCareWithUser.setOwner((User) userService.findByUsername(employee.getUsername()));
        afterSchoolCareService.save(testAfternoonCareWithUser);

        Attendance attendance = new Attendance();
        attendance.setNote("Darf früher gehen");
        attendance.setArrivalTime(LocalDateTime.of(2020, 4, 3, 12, 2));
        attendance.setAfterSchoolCare(testAfternoonCareWithUser);
        attendance.setChild((User) userService.findByUsername("Child_Test"));
        attendanceService.save(attendance);

        testAfternoonCareWithUser.addAttendance(attendance);
        afterSchoolCareService.save(testAfternoonCareWithUser);

        testAfternoonCareWithDifferentUser = new AfternoonCare();
        testAfternoonCareWithDifferentUser.setName("Test-Nachmittagsbetreuung (2)");
        testAfternoonCareWithDifferentUser.setOwner((User) userService.findByUsername("Employee_Test2"));
        afterSchoolCareService.save(testAfternoonCareWithDifferentUser);
    }

    @Test
    public void testGetAfternoonCaresWithEmployeeAuthority() {
        ValidatableResponse response = super.sendGetRequestWithAuth(employee, TestEmployeeControllerPath.AFTER_SCHOOL_CARES.getUri()).statusCode(200);

        AfterSchoolCareDTO[] resultAfternoonCaresArray = response.extract().body().as(AfterSchoolCareDTO[].class);
        List<AfterSchoolCareDTO> resultAfternoonCares = Arrays.asList(resultAfternoonCaresArray);
        assertTrue(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCareWithUser.getId())));
        assertFalse(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCareWithDifferentUser.getId())));
    }
}
