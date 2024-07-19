package net.internalerror.rest.endpoint;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface RecipeBookEndpoint {

    @PutMapping
    ResponseEntity<CreateResponse> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid CreateRequest request);

    record CreateRequest(
            @NotEmpty(message = "NAME_CANNOT_BE_EMPTY") @Length(message = "NAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS", max = 50) String name
    ) {

    }

    record CreateResponse(String identifier) {

    }

    @PatchMapping
    void rename(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid RenameRequest request);

    record RenameRequest(
            @NotEmpty(message = "NAME_CANNOT_BE_EMPTY") @Length(message = "NAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS", max = 50) String name,
            @NotEmpty(message = "IDENTIFIER_CANNOT_BE_EMPTY") String identifier
    ) {

    }

    //TODO: implement deletion

}
