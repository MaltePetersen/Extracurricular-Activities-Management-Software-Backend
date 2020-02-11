package com.main.assured;

import com.main.data.TestParentControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.Role;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;

public class AssuredParentControllerTest extends AbstractAssuredTest {
    private IUserDTO parentDTO = TestUserData.TEST_PARENT_7.getUserDTO();

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(parentDTO.getUsername());
        if (myUser == null) {
            User user = (User) User.UserBuilder.next().withDto(this.parentDTO).build();
            user.setPassword(encoder.encode(user.getPassword()));
            Role role = roleRepository.findByName(UserRole.ROLE_PARENT.toString());
            user = userService.update(user, UserRole.ROLE_PARENT);
        }
    }

    @Test
    public void getChildrenByParentUserName(){
        ValidatableResponse response = super.sendGetRequestWithAuth(parentDTO, TestParentControllerPath.CHILDREN.getUri()).then().assertThat().statusCode(200);

        String body = response.extract().body().asString();
        System.out.println(body);
    }
}
