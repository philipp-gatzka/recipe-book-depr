package net.internalerror.service;

import net.internalerror.ApiTest;
import net.internalerror.endpoint.AuthEndpoint;
import org.junit.jupiter.api.Test;

import static net.internalerror.Tables.USER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest extends ApiTest {

    @Test
    void givenNothing_whenRegister_ThenUserShouldBeRegistered() {
        AuthEndpoint.RegisterRequest request = new AuthEndpoint.RegisterRequest(string(10), string(10), string(10) + "@gmail.com", string(10));

        authController.register(request);

        assertTrue(userRepository.exists(USER.EMAIL.eq(request.email())));
    }

    @Test
    void givenRegisteredUser_whenLogin_thenShouldReturnToken() {
        TestUser user = createUser();
        AuthEndpoint.LoginRequest request = new AuthEndpoint.LoginRequest(user.email(), user.password());

        assertDoesNotThrow(() -> authController.login(request));
    }

}