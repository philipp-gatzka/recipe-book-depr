package net.internalerror.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.internalerror.Messages;
import net.internalerror.endpoint.UserEndpoint;
import net.internalerror.exception.BadRequestException;
import net.internalerror.repository.UserRepository;
import net.internalerror.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.internalerror.Tables.USER;

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
