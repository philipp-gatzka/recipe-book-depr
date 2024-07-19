package net.internalerror.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.internalerror.endpoint.AuthEndpoint;
import net.internalerror.exception.BadRequestException;
import net.internalerror.helper.DataHelper;
import net.internalerror.repository.UserRepository;
import net.internalerror.service.AuthService;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.internalerror.Messages.*;
import static net.internalerror.Tables.USER;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthEndpoint {

    private final AuthService service;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final DataHelper dataHelper;

    @Override
    public void register(RegisterRequest request) {
        if (dataHelper.userExists(request.email())) {
            throw new BadRequestException(EMAIL_IS_ALREADY_REGISTERED);
        }

        service.register(request);
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        if (!dataHelper.userExists(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_REGISTERED);
        }

        if (dataHelper.userIsVerified(request.email())) {
            throw new BadRequestException(EMAIL_IS_ALREADY_VERIFIED);
        }

        User user = userRepository.get(USER.EMAIL.eq(request.email()));
        if (!passwordEncoder.matches(request.code(), user.getEmailVerificationCode())) {
            throw new BadRequestException(EMAIL_VERIFICATION_CODE_IS_INVALID);
        }

        service.verifyEmail(request);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        if (!dataHelper.userExists(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_REGISTERED);
        }

        if (!dataHelper.userIsVerified(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_VERIFIED);
        }

        return service.login(request);
    }

    @Override
    public void requestUpdatePassword(RequestUpdatePasswordRequest request) {
        if (!dataHelper.userExists(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_REGISTERED);
        }

        if (!dataHelper.userIsVerified(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_VERIFIED);
        }

        service.requestUpdatePassword(request);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        if (!dataHelper.userExists(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_REGISTERED);
        }

        if (!dataHelper.userIsVerified(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_VERIFIED);
        }

        User user = userRepository.get(USER.EMAIL.eq(request.email()));
        if (user.getPasswordResetCode() == null || !passwordEncoder.matches(request.code(), user.getPasswordResetCode())) {
            throw new BadRequestException(PASSWORD_RESET_CODE_IS_INVALID);
        }

        service.updatePassword(request);
    }

}
