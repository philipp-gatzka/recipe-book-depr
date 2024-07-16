package net.internalerror.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.internalerror.endpoint.UserEndpoint;
import net.internalerror.repository.UserRepository;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static net.internalerror.Tables.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserEndpoint {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    public void update(String token, UpdateUserRequest request) {
        User user = jwtService.extractUser(token);
        userRepository.update(entry -> entry
                        .setFirstname(StringUtils.hasText(request.firstname()) ? request.firstname() : entry.getFirstname())
                        .setLastname(StringUtils.hasText(request.lastname()) ? request.lastname() : entry.getLastname()),
                USER.ID.eq(user.getId())
        );
        User usern = jwtService.extractUser(token);
        log.info(usern.toString());
    }


}
