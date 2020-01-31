package com.main.assured;

import com.main.data.TestSchoolCoordinatorPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.afterSchoolCare.WorkingGroup;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;
import java.util.HashMap;


public class AssuredSchoolCoordinatorTest extends AbstractAssuredTest {

    private IUserDTO userDTO = TestUserData.TEST_SCHOOL_COORDINATOR.getUserDTO();

    private String id;

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(userDTO.getUsername());
        if(myUser != null)
            return;

        User user = (User) User.UserBuilder.next().withDto(userDTO).build();
        user.setPassword(encoder.encode(user.getPassword()));
        user = userService.update(user, UserRole.ROLE_SCHOOLCOORDINATOR);

        WorkingGroup workingGroup = new WorkingGroup();
        workingGroup.setName("Working Group");
        workingGroup.setOwner(user);

        afterSchoolCareService.save(workingGroup);

        id = workingGroup.getId().toString();
    }

    @After
    @Transactional
    public void teardown(){
        IUser user = userService.findByUsername(userDTO.getUsername());
        if(user == null)
            return;
        userService.deleteUserById(user.getId());
    }


    @Test
    public void getAllWorkingGroupsTest() {

        System.out.println(userService.findAll());
        super.sendGetRequestWithAuth(userDTO, TestSchoolCoordinatorPath.GET_SCHOOLS.getUri()).statusCode(200)
                .log().body();
    }

    @Test
    public void getWorkingGroupByIdTest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        super.sendGetRequestWithAuthAndParams(userDTO, TestSchoolCoordinatorPath.GET_SCHOOL.getUri(), params).statusCode(200)
                .log().body();
    }

    @Test
    public void addWorkingGroup(){



    }



}
