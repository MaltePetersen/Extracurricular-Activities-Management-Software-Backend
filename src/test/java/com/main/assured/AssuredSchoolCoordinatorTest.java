package com.main.assured;

import com.main.data.TestSchoolCoordinatorPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.afterSchoolCare.WorkingGroup;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

public class AssuredSchoolCoordinatorTest extends AbstractAssuredTest {

    private IUserDTO sc = TestUserData.TEST_SCHOOL_COORDINATOR.getUserDTO();

    private String id;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(sc.getUsername());
        if(myUser != null)
            return;

        User user = (User) User.UserBuilder.next().withDto(this.sc).build();
        user.setPassword(encoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(UserRole.ROLE_SCHOOLCOORDINATOR.toString());
        user.getRoles().add(role);

        userService.update(user);

        WorkingGroup workingGroup = new WorkingGroup();
        workingGroup.setName("Working Group");
        workingGroup.setOwner(user);

        afterSchoolCareService.save(workingGroup);
        id = workingGroup.getId().toString();
    }


    @Test
    public void getAllWorkingGroupsTest() {
        System.out.println(sc);
        super.sendGetRequestWithAuth(sc, TestSchoolCoordinatorPath.GET_SCHOOLS.getUri()).statusCode(200)
                .log().body();
    }

    @Test
    public void getWorkingGroupByIdTest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        super.sendGetRequestWithAuthAndParams(sc, TestSchoolCoordinatorPath.GET_SCHOOL.getUri(), params).statusCode(200)
                .log().body();
    }

    @Test
    public void addWorkingGroup(){



    }



}
