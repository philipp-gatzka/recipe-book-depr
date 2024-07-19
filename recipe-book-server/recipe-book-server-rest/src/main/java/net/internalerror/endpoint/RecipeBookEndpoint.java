package net.internalerror.endpoint;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static net.internalerror.Messages.*;

public interface RecipeBookEndpoint {

    @PutMapping
    ResponseEntity<CreateResponse> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid RecipeBookEndpoint.CreateRequest request);

    record CreateRequest(
            @NotEmpty(message = NAME_CANNOT_BE_EMPTY) @Length(message = NAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String name) {

    }

    record CreateResponse(String identifier) {

    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void rename(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid RecipeBookEndpoint.RenameRequest request);

    record RenameRequest(@NotEmpty(message = IDENTIFIER_CANNOT_BE_EMPTY) String identifier,
                         @NotEmpty(message = NAME_CANNOT_BE_EMPTY) @Length(message = NAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String name) {

    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid DeleteRecipeBookRequest request);

    record DeleteRecipeBookRequest(@NotEmpty(message = IDENTIFIER_CANNOT_BE_EMPTY) String identifier) {

    }
}
