package com.main.assured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.main.data.TestSchoolCoordinatorPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.afterSchoolCare.WorkingGroup;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import org.hibernate.jdbc.Work;
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

        registerUser(userDTO);
        User user = (User) userService.findByUsername( userDTO.getUsername() );


        WorkingGroup workingGroup = new WorkingGroup();
        workingGroup.setName("Working Group");
        workingGroup.setOwner(user);
        afterSchoolCareService.save(workingGroup);

        id = workingGroup.getId().toString();
        userService.update(user);
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
        super.sendGetRequestWithAuth(userDTO, TestSchoolCoordinatorPath.GET_SCHOOLS.getUri())
                .then().assertThat()
                .statusCode(200)
                .log().body();
    }

    @Test
    public void getWorkingGroupByIdTest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        super.sendGetRequestWithAuthAndParams(userDTO, TestSchoolCoordinatorPath.GET_SCHOOL.getUri(), params)
                .then().assertThat().statusCode(200)
                .log().body();
    }

    @Test
    public void addWorkingGroup() throws JsonProcessingException {
        WorkingGroup workingGroup = new WorkingGroup();
        workingGroup.setName("New Working Group");

        String json =  mapToJson(workingGroup);

        super.sendPatchWithAuthAndUserNameAndJSON( userDTO, TestSchoolCoordinatorPath.ADD_SCHOOL.getUri(), json).assertThat().statusCode(202);



    }



}
