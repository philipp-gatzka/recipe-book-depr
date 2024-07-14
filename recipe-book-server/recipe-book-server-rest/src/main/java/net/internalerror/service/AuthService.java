package net.internalerror.service;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.AuthEndpoint;
import net.internalerror.repository.UserRepository;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static net.internalerror.Tables.USER;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthEndpoint {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {
        userRepository.insert(entry -> {
            entry.setFirstname(request.firstname());
            entry.setLastname(request.lastname());
            entry.setEmail(request.email());
            entry.setPassword(passwordEncoder.encode(request.password()));
            return entry;
        });
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.get(USER.EMAIL.eq(request.email()));
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok().body(new LoginResponse(token));
    }

}
