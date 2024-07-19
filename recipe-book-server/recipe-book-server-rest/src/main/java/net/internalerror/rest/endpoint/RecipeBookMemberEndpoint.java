package net.internalerror.rest.endpoint;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static net.internalerror.Messages.EMAIL_CANNOT_BE_EMPTY;
import static net.internalerror.Messages.IDENTIFIER_DOES_NOT_EXIST;

public interface RecipeBookMemberEndpoint {

    @PutMapping
    void invite(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid InviteRequest request);

    record InviteRequest(
            @NotEmpty(message = EMAIL_CANNOT_BE_EMPTY) String email,
            @NotEmpty(message = IDENTIFIER_DOES_NOT_EXIST) String identifier
    ) {

    }

    @PatchMapping
    void join(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid JoinRequest request);

    record JoinRequest(
            @NotEmpty(message = IDENTIFIER_DOES_NOT_EXIST) String identifier
    ) {

    }
}
