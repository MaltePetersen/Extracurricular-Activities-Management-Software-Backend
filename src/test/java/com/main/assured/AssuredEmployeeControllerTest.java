package com.main.assured;

import com.main.data.TestEmployeeControllerPath;
import com.main.data.TestUserData;
import com.main.dto.AfterSchoolCareDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Attendance;
import com.main.model.Role;
import com.main.model.School;
import com.main.model.User;
import com.main.model.afterSchoolCare.AfternoonCare;
import com.main.model.afterSchoolCare.Remedial;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AssuredEmployeeControllerTest extends AbstractAssuredTest {
    private AfternoonCare testAfternoonCareWithUserSchool1;
    private Remedial testRemedialWithUserSchool2;
    private AfternoonCare testAfternoonCareWithDifferentUser;

    private School school1;
    private School school2;

    private IUserDTO employee = TestUserData.TEST_EMPLOYEE.getUserDTO();

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(employee.getUsername());
        if(myUser == null) {
            User user = (User) User.UserBuilder.next().withDto(this.employee).build();
            user.setPassword(encoder.encode(user.getPassword()));
            Role role = roleRepository.findByName(UserRole.ROLE_EMPLOYEE.toString());
            user = userService.update(user, UserRole.ROLE_EMPLOYEE);
        }

        // Testdaten erstellen
        school1 = new School("Test-Schule 1", "Test-Adresse 1");
        schoolService.save(school1);

        school2 = new School("Test-Schule 2", "Test-Adresse 2");
        schoolService.save(school2);

        testAfternoonCareWithUserSchool1 = new AfternoonCare();
        testAfternoonCareWithUserSchool1.setName("Test-Nachmittagsbetreuung");
        testAfternoonCareWithUserSchool1.setOwner((User) userService.findByUsername(employee.getUsername()));
        testAfternoonCareWithUserSchool1.setParticipatingSchool(school1);
        afterSchoolCareService.save(testAfternoonCareWithUserSchool1);

        Attendance attendance = new Attendance();
        attendance.setNote("Darf früher gehen");
        attendance.setArrivalTime(LocalDateTime.of(2020, 4, 3, 12, 2));
        attendance.setAfterSchoolCare(testAfternoonCareWithUserSchool1);
        attendance.setChild((User) userService.findByUsername("Child_Test"));
        attendanceService.save(attendance);

        testAfternoonCareWithUserSchool1.addAttendance(attendance);
        afterSchoolCareService.save(testAfternoonCareWithUserSchool1);

        testRemedialWithUserSchool2 = new Remedial();
        testRemedialWithUserSchool2.setName("Bio-Nachhilfe");
        testRemedialWithUserSchool2.setOwner((User) userService.findByUsername(employee.getUsername()));
        testRemedialWithUserSchool2.setParticipatingSchool(school2);
        afterSchoolCareService.save(testRemedialWithUserSchool2);

        testAfternoonCareWithDifferentUser = new AfternoonCare();
        testAfternoonCareWithDifferentUser.setName("Test-Nachmittagsbetreuung (2)");
        testAfternoonCareWithDifferentUser.setOwner((User) userService.findByUsername("Employee_Test2"));
        afterSchoolCareService.save(testAfternoonCareWithDifferentUser);
    }

    @Test
    public void testGetAfternoonCaresWithEmployeeAuthority() {
        ValidatableResponse response = super.sendGetRequestWithAuth(employee, TestEmployeeControllerPath.AFTER_SCHOOL_CARES.getUri()).statusCode(200);

        List<AfterSchoolCareDTO> resultAfternoonCares = Arrays.asList(response.extract().body().as(AfterSchoolCareDTO[].class));

        // verifiziert, dass korrekt gefiltert wurde, so dass u.a. keine AfterSchoolCares von anderen Employees enthalten sind
        assertTrue(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCareWithUserSchool1.getId())));
        assertTrue(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testRemedialWithUserSchool2.getId())));
        assertFalse(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCareWithDifferentUser.getId())));
    }

    @Test
    public void testGetAfterSchoolCaresWithEmployeeAuthorityAndSchoolFilter() {
        ValidatableResponse response = super.sendGetRequestWithAuth(employee, TestEmployeeControllerPath.AFTER_SCHOOL_CARES.getUri() + "?school=" + testAfternoonCareWithUserSchool1.getParticipatingSchool().getId()).statusCode(200);

        List<AfterSchoolCareDTO> resultAfternoonCares = Arrays.asList(response.extract().body().as(AfterSchoolCareDTO[].class));

        assertTrue(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testAfternoonCareWithUserSchool1.getId())));
        assertFalse(resultAfternoonCares.stream().anyMatch(afterSchoolCareDTO -> afterSchoolCareDTO.getId().equals(testRemedialWithUserSchool2.getId())));
    }

    @After
    @Transactional
    public void teardown() {
        userService.deleteByName(employee.getUsername());

        schoolService.deleteById(school1.getId());
        schoolService.deleteById(school2.getId());

        afterSchoolCareService.deleteById(testAfternoonCareWithDifferentUser.getId());
    }
}
