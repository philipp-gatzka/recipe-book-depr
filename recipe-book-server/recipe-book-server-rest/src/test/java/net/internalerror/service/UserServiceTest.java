package net.internalerror.service;

import net.internalerror.ApiTest;
import net.internalerror.endpoint.UserEndpoint;
import net.internalerror.tables.records.User;
import org.junit.jupiter.api.Test;

import static net.internalerror.Tables.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest extends ApiTest {

    @Test
    void givenAuthenticatedUser_whenUpdateWithFirstnameAndLastname_thenShouldUpdateFirstnameAndLastname() {
        AuthenticatedUser authenticatedUser = createAuthenticatedUser();
        UserEndpoint.UpdateUserRequest request = new UserEndpoint.UpdateUserRequest(string(10), string(10));
        userController.update(authenticatedUser.token(), request);

        assertEquals(request.firstname(), userRepository.get(USER.EMAIL.eq(authenticatedUser.email())).getFirstname());
        assertEquals(request.lastname(), userRepository.get(USER.EMAIL.eq(authenticatedUser.email())).getLastname());
    }

    @Test
    void givenAuthenticatedUser_whenUpdate_thenShouldUpdateNotFirstnameAndLastname() {
        AuthenticatedUser authenticatedUser = createAuthenticatedUser();
        User user = userRepository.get(USER.EMAIL.eq(authenticatedUser.email()));
        UserEndpoint.UpdateUserRequest request = new UserEndpoint.UpdateUserRequest("", "");
        userController.update(authenticatedUser.token(), request);

        User updatedUser = userRepository.get(USER.EMAIL.eq(authenticatedUser.email()));
        assertEquals(user.getFirstname(), updatedUser.getFirstname());
        assertEquals(user.getLastname(), updatedUser.getLastname());
    }

}
