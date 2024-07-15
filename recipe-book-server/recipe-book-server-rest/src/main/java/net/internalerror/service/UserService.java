package net.internalerror.service;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.UserEndpoint;
import net.internalerror.repository.UserRepository;
import net.internalerror.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserEndpoint {


    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public void update(String token, UpdateUserRequest request) {
        String email = jwtService.extractEmail(token);


    }

}
