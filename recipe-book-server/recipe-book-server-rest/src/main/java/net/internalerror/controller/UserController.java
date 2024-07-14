package net.internalerror.controller;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.UserEndpoint;
import net.internalerror.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserEndpoint {

    private final UserRepository userRepository;

    @Override
    public void update(UpdateUserRequest request) {

    }


}
