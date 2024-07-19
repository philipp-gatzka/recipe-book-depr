package net.internalerror.rest.endpoint;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static net.internalerror.Messages.*;

public interface AuthEndpoint {

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void register(@RequestBody @Valid RegisterRequest request);

    record RegisterRequest(
            @NotEmpty(message = FIRSTNAME_CANNOT_BE_EMPTY) @Length(message = FIRSTNAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String firstname,
            @NotEmpty(message = LASTNAME_CANNOT_BE_EMPTY) @Length(message = LASTNAME_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String lastname,
            @NotEmpty(message = EMAIL_CANNOT_BE_EMPTY) @Length(message = EMAIL_CANNOT_BE_LONGER_THAN_100_CHARACTERS, max = 100) String email,
            @NotEmpty(message = PASSWORD_CANNOT_BE_EMPTY) @Length(message = PASSWORD_CANNOT_BE_LONGER_THAN_50_CHARACTERS, max = 50) String password) {

    }

    @PutMapping(value = "verify", consumes = MediaType.APPLICATION_JSON_VALUE)
    void verifyEmail(@RequestBody @Valid VerifyEmailRequest request);

    record VerifyEmailRequest(@NotEmpty(message = EMAIL_CANNOT_BE_EMPTY) String email,
                              @NotEmpty(message = EMAIL_VERIFICATION_CODE_CANNOT_BE_EMPTY) String code) {

    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request);

    record LoginRequest(@NotEmpty(message = EMAIL_CANNOT_BE_EMPTY) String email,
                        @NotEmpty(message = PASSWORD_CANNOT_BE_EMPTY) String password) {

    }

    record LoginResponse(String token) {

    }

    @GetMapping("update-password")
    void requestUpdatePassword(@RequestBody @Valid RequestUpdatePasswordRequest request);

    record RequestUpdatePasswordRequest(@NotEmpty(message = EMAIL_CANNOT_BE_EMPTY) String email) {

    }

    @PatchMapping("update-password")
    void updatePassword(@RequestBody @Valid UpdatePasswordRequest request);

    record UpdatePasswordRequest(@NotEmpty(message = EMAIL_CANNOT_BE_EMPTY) String email,
                                 @NotEmpty(message = PASSWORD_RESET_CODE_CANNOT_BE_EMPTY) String code,
                                 @NotEmpty(message = PASSWORD_CANNOT_BE_EMPTY) String password) {

    }

}
