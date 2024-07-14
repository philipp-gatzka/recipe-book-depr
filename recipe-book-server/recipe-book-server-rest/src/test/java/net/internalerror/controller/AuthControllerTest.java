package net.internalerror.controller;

import net.internalerror.ApiTest;
import net.internalerror.Messages;
import net.internalerror.endpoint.AuthEndpoint;
import net.internalerror.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest extends ApiTest {

    @Test
    void givenRegisteredEmail_whenRegister_thenShouldThrowEMAIL_IS_ALREADY_REGISTERED() {
        TestUser user = createUser();
        AuthEndpoint.RegisterRequest request = new AuthEndpoint.RegisterRequest(
                string(10),
                string(10),
                user.email(),
                string(10)
        );

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> authController.register(request));
        assertEquals(Messages.EMAIL_IS_ALREADY_REGISTERED, badRequestException.getReason());
    }

    @Test
    void givenUnregisteredEmail_whenRegister_thenShouldThrowEMAIL_IS_NOT_REGISTERED() {
        AuthEndpoint.LoginRequest request = new AuthEndpoint.LoginRequest(
                string(10) + "@gmail.com",
                string(10)
        );

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> authController.login(request));
        assertEquals(Messages.EMAIL_IS_NOT_REGISTERED, badRequestException.getReason());
    }

}