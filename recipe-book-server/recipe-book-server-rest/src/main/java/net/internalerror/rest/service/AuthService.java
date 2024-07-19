package net.internalerror.rest.service;

import lombok.RequiredArgsConstructor;
import net.internalerror.rest.endpoint.AuthEndpoint;
import net.internalerror.helper.EmailHelper;
import net.internalerror.repository.UserRepository;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static net.internalerror.Tables.USER;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthEndpoint {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailHelper emailHelper;


    @Override
    public void register(RegisterRequest request) {
        String code = UUID.randomUUID().toString();
        userRepository.insert(entry -> entry
                .setFirstname(request.firstname())
                .setLastname(request.lastname())
                .setEmail(request.email())
                .setPassword(passwordEncoder.encode(request.password()))
                .setEmailVerificationCode(passwordEncoder.encode(code))
        );

        emailHelper.sendEmail(request.email(), "Verify Email", "Verification Code: " + code);
        userRepository.update(entry -> entry.setPasswordResetCode(passwordEncoder.encode(code)), USER.EMAIL.eq(request.email()));
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        userRepository.update(entry -> entry.setEmailVerificationCode(null), USER.EMAIL.eq(request.email()));
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.get(USER.EMAIL.eq(request.email()));
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok().body(new LoginResponse(token));
    }

    @Override
    public void requestUpdatePassword(RequestUpdatePasswordRequest request) {
        String code = UUID.randomUUID().toString();
        userRepository.update(entry -> entry.setPasswordResetCode(passwordEncoder.encode(code)), USER.EMAIL.eq(request.email()));
        emailHelper.sendEmail(request.email(), "Reset Password", "Password reset code: " + code);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        userRepository.update(entry -> entry.setPassword(request.password()).setPasswordResetCode(null), USER.EMAIL.eq(request.email()));
    }


}
