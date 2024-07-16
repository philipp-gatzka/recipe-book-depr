package net.internalerror.endpoint;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static net.internalerror.Messages.NAME_CANNOT_BE_EMPTY;
import static net.internalerror.Messages.NAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS;

public interface RecipeBookEndpoint {

    @PutMapping
    void create(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody @Valid CreateRecipeBookRequest request);

    record CreateRecipeBookRequest(@NotEmpty(message = NAME_CANNOT_BE_EMPTY) @Length(message = NAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String name) {

    }
}
