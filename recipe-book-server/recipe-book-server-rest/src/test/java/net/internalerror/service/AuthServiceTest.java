package net.internalerror.service;

import com.icegreen.greenmail.util.GreenMailUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import net.internalerror.ApiTest;
import net.internalerror.endpoint.AuthEndpoint;
import net.internalerror.tables.records.User;
import org.junit.jupiter.api.Test;

import static net.internalerror.Tables.USER;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AuthServiceTest extends ApiTest {

    @Test
    void givenNothing_whenRegister_ThenUserShouldBeRegistered() {
        AuthEndpoint.RegisterRequest request = new AuthEndpoint.RegisterRequest(string(10), string(10), string(10) + "@gmail.com", string(10));
        authController.register(request);
        assertTrue(userRepository.exists(USER.EMAIL.eq(request.email())));

        assertEquals(1, greenMail.getReceivedMessages().length);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        String code = GreenMailUtil.getBody(receivedMessage).substring(19);

        assertNotNull(code);
        User user = userRepository.get(USER.EMAIL.eq(request.email()));
        assertTrue(passwordEncoder.matches(code, user.getEmailVerificationCode()));
    }

    @Test
    void givenRegisteredUser_whenVerify_ThenUserShouldBeVerified() {
        RegisteredUser registeredUser = createRegisteredUser();

        AuthEndpoint.VerifyEmailRequest request = new AuthEndpoint.VerifyEmailRequest(registeredUser.email(), registeredUser.code());
        authController.verifyEmail(request);

        User user = userRepository.get(USER.EMAIL.eq(request.email()));
        assertNull(user.getEmailVerificationCode());
    }

    @Test
    void givenVerifiedUser_whenLogin_thenShouldReturnToken() {
        VerifiedUser verifiedUser = createVerifiedUser();
        AuthEndpoint.LoginRequest request = new AuthEndpoint.LoginRequest(verifiedUser.email(), verifiedUser.password());
        assertNotNull(assertDoesNotThrow(() -> authController.login(request)));
    }

    @Test
    void givenVerifiedUser_whenRequestUpdatePassword_thenShouldSendEmailWithCode() {
        VerifiedUser verifiedUser = createVerifiedUser();
        AuthEndpoint.RequestUpdatePasswordRequest request = new AuthEndpoint.RequestUpdatePasswordRequest(verifiedUser.email());
        authController.requestUpdatePassword(request);

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[1];
        String code = GreenMailUtil.getBody(receivedMessage).substring(21);

        assertNotNull(code);
        User user = userRepository.get(USER.EMAIL.eq(request.email()));
        assertTrue(passwordEncoder.matches(code, user.getPasswordResetCode()));
    }

    @Test
    void givenVerifiedUserAndPasswordUpdateCode_whenUpdatePassword_thenShouldUpdatePassword(){
        VerifiedUser verifiedUser = createVerifiedUser();
        AuthEndpoint.RequestUpdatePasswordRequest request = new AuthEndpoint.RequestUpdatePasswordRequest(verifiedUser.email());
        authController.requestUpdatePassword(request);

        String oldPassword = userRepository.get(USER.EMAIL.eq(request.email())).getPassword();


        MimeMessage receivedMessage = greenMail.getReceivedMessages()[1];
        String code = GreenMailUtil.getBody(receivedMessage).substring(21);
        AuthEndpoint.UpdatePasswordRequest updatePasswordRequest = new AuthEndpoint.UpdatePasswordRequest(verifiedUser.email(), code, string(25));
        authController.updatePassword(updatePasswordRequest);
        User user = userRepository.get(USER.EMAIL.eq(verifiedUser.email()));
        assertNull(user.getPasswordResetCode());
        assertNotEquals(oldPassword, user.getPassword());
    }

}