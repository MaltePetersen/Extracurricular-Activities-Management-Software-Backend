package com.main.assured;

import com.main.data.TestParentControllerPath;
import com.main.data.TestUserData;
import com.main.dto.interfaces.IUserDTO;
import com.main.model.User;
import com.main.model.interfaces.IUser;
import com.main.model.user.UserRole;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

public class AssuredParentControllerTest extends AbstractAssuredTest {
    private IUserDTO parentDTO = TestUserData.TEST_PARENT_7.getUserDTO();

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();

        IUser myUser = userService.findByUsername(parentDTO.getUsername());
        if(myUser == null) {
            User parent = (User) User.UserBuilder.next().withDto(parentDTO).build();
            parent.setPassword(encoder.encode(parent.getPassword()));
            parent = userService.update(parent, UserRole.ROLE_PARENT);
        }
    }

    @Test
    public void getChildrenByParentUserName(){
        Response response = super.sendGetRequestWithAuth(parentDTO, TestParentControllerPath.CHILDREN.getUri());

        String body = response.body().asString();
        System.out.println(body);
        assertEquals(200, response.statusCode());

    }
}
