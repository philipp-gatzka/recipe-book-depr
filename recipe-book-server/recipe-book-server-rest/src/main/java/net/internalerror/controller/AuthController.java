package net.internalerror.controller;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.AuthEndpoint;
import net.internalerror.exception.BadRequestException;
import net.internalerror.repository.UserRepository;
import net.internalerror.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.internalerror.Messages.EMAIL_IS_ALREADY_REGISTERED;
import static net.internalerror.Messages.EMAIL_IS_NOT_REGISTERED;
import static net.internalerror.Tables.USER;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthEndpoint {

    private final AuthService service;

    private final UserRepository userRepository;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.exists(USER.EMAIL.eq(request.email()))) {
            throw new BadRequestException(EMAIL_IS_ALREADY_REGISTERED);
        }
        service.register(request);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        if (!userRepository.exists(USER.EMAIL.eq(request.email()))) {
            throw new BadRequestException(EMAIL_IS_NOT_REGISTERED);
        }
        return service.login(request);
    }

}
