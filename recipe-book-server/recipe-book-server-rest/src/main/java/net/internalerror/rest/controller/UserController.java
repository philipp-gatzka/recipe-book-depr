package net.internalerror.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.internalerror.rest.endpoint.UserEndpoint;
import net.internalerror.rest.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserEndpoint {

    private final UserService userService;

    @Override
    public void update(String token, UpdateUserRequest request) {
        userService.update(token, request);
    }

}
