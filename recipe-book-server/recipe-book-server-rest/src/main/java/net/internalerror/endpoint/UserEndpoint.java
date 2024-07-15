package net.internalerror.endpoint;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static net.internalerror.Messages.*;

public interface UserEndpoint {

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void update(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid UpdateUserRequest request);

    record UpdateUserRequest(
            @Length(message = FIRSTNAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String firstname,
            @Length(message = LASTNAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String lastname,
            @Length(message = EMAIL_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 100) String email) {

    }
}
