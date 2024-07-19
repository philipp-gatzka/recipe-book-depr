package net.internalerror.endpoint;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static net.internalerror.Messages.EMAIL_CANNOT_BE_EMPTY;
import static net.internalerror.Messages.IDENTIFIER_CANNOT_BE_EMPTY;

public interface RecipeBookMembershipEndpoint {

    @PutMapping
    void invite(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid InviteMemberRequest request);

    record InviteMemberRequest(@NotEmpty(message = EMAIL_CANNOT_BE_EMPTY) String email,
                               @NotEmpty(message = IDENTIFIER_CANNOT_BE_EMPTY) String identifier) {

    }

    @GetMapping
    ResponseEntity<ListMembershipsResponse> list(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    record ListMembershipsResponse(List<Data> data) {
       public record Data(String name, String identifier, String permission) {

        }
    }

    //@PatchMapping
    //void join();

    //@PatchMapping
    //void kick();

    //@PatchMapping
    //void promote();
}
