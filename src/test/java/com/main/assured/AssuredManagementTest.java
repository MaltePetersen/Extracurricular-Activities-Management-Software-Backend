package com.main.assured;

import com.main.data.TestManagementControllerPath;
import com.main.data.TestUserData;
import com.main.dto.UserDTO;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;

public class AssuredManagementTest extends AbstractAssuredTest {

    private IUserDTO userDTO = TestUserData.TEST_MANAGEMENT.getUserDTO();

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
        IUser iUser = userService.findByUsername(userDTO.getUsername());
        if (iUser != null)
            return;

        User user = (User) User.UserBuilder.next().withDto(this.userDTO).build();
        Role role = roleRepository.findByName(UserRole.ROLE_MANAGEMENT.toString());

        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.addRole(role);
        roleRepository.save(role);


        userService.update(user);
    }

    @After
    public void teardown() {
        IUser iUser = userService.findByUsername(userDTO.getUsername());
        if (iUser == null)
            return;

        userService.deleteUserById(iUser.getId());
    }


    @Test
    public void getAllUsersTest() {
        ValidatableResponse response = super.sendGetRequestWithAuth(userDTO, TestManagementControllerPath.GET_ALL_USERS.getUri());
        response.log().body();
    }

}
