package net.internalerror.controller;

import net.internalerror.ApiTest;
import net.internalerror.rest.endpoint.AuthEndpoint;
import net.internalerror.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static net.internalerror.Messages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest extends ApiTest {

    @Test
    void givenRegisteredEmail_whenRegister_thenShouldThrowEMAIL_IS_ALREADY_REGISTERED() {
        RegisteredUser registeredUser = createRegisteredUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.register(new AuthEndpoint.RegisterRequest(string(10), string(10), registeredUser.email(), string(10))));
        assertEquals(EMAIL_IS_ALREADY_REGISTERED, exception.getReason());
    }

    @Test
    void givenNothing_whenVerifyEmail_thenShouldThrowEMAIL_IS_NOT_REGISTERED() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.verifyEmail(new AuthEndpoint.VerifyEmailRequest(string(10) + "@gmail.com", "HelloWorld")));
        assertEquals(EMAIL_IS_NOT_REGISTERED, exception.getReason());
    }

    @Test
    void givenVerifiedUser_whenVerifyEmail_thenShouldThrowEMAIL_IS_ALREADY_VERIFIED() {
        VerifiedUser verifiedUser = createVerifiedUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.verifyEmail(new AuthEndpoint.VerifyEmailRequest(verifiedUser.email(), "HelloWorld")));
        assertEquals(EMAIL_IS_ALREADY_VERIFIED, exception.getReason());
    }

    @Test
    void givenRegisteredUser_whenVerifyEmailWithInvalidCode_thenShouldThrowEMAIL_VERIFICATION_CODE_IS_INVALID() {
        RegisteredUser registeredUser = createRegisteredUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.verifyEmail(new AuthEndpoint.VerifyEmailRequest(registeredUser.email(), "HelloWorld")));
        assertEquals(EMAIL_VERIFICATION_CODE_IS_INVALID, exception.getReason());
    }

    @Test
    void givenNothing_whenLogin_thenShouldThrowEMAIL_IS_NOT_REGISTERED() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.login(new AuthEndpoint.LoginRequest(string(10) + "@gmail.com", string(10))));
        assertEquals(EMAIL_IS_NOT_REGISTERED, exception.getReason());
    }

    @Test
    void givenRegisteredUser_whenLogin_thenShouldThrowEMAIL_IS_NOT_VERIFIED() {
        RegisteredUser registeredUser = createRegisteredUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.login(new AuthEndpoint.LoginRequest(registeredUser.email(), registeredUser.password())));
        assertEquals(EMAIL_IS_NOT_VERIFIED, exception.getReason());
    }

    @Test
    void givenNothing_whenRequestUpdatePassword_thenShouldThrowEMAIL_IS_NOT_REGISTERED() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.requestUpdatePassword(new AuthEndpoint.RequestUpdatePasswordRequest(string(10) + "@gmail.com")));
        assertEquals(EMAIL_IS_NOT_REGISTERED, exception.getReason());
    }

    @Test
    void givenRegisteredUser_whenRequestUpdatePassword_thenShouldThrowEMAIL_IS_NOT_VERIFIED() {
        RegisteredUser registeredUser = createRegisteredUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.requestUpdatePassword(new AuthEndpoint.RequestUpdatePasswordRequest(registeredUser.email())));
        assertEquals(EMAIL_IS_NOT_VERIFIED, exception.getReason());
    }

    @Test
    void givenNothing_whenUpdatePassword_thenShouldThrowEMAIL_IS_NOT_REGISTERED() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.updatePassword(new AuthEndpoint.UpdatePasswordRequest(string(10) + "@gmail.com", string(10), string(10))));
        assertEquals(EMAIL_IS_NOT_REGISTERED, exception.getReason());
    }

    @Test
    void givenRegisteredUser_whenUpdatePassword_thenShouldThrowEMAIL_IS_NOT_VERIFIED() {
        RegisteredUser registeredUser = createRegisteredUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.updatePassword(new AuthEndpoint.UpdatePasswordRequest(registeredUser.email(), string(10), string(10))));
        assertEquals(EMAIL_IS_NOT_VERIFIED, exception.getReason());
    }

    @Test
    void givenVerifiedUser_whenUpdatePassword_thenShouldThrowPASSWORD_RESET_CODE_IS_INVALID() {
        VerifiedUser verifiedUser = createVerifiedUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> authController.updatePassword(new AuthEndpoint.UpdatePasswordRequest(verifiedUser.email(), string(10), string(10))));
        assertEquals(PASSWORD_RESET_CODE_IS_INVALID, exception.getReason());
    }

}