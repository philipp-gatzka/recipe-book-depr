package net.internalerror.controller;

import net.internalerror.ApiTest;
import net.internalerror.endpoint.RecipeBookEndpoint;
import net.internalerror.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static net.internalerror.Messages.IDENTIFIER_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecipeBookControllerTest extends ApiTest {

    @Test
    void givenAuthenticatedUserAndInvalidIdentifier_whenRename_thenShouldThrowIDENTIFIER_DOES_NOT_EXIST() {
        AuthenticatedUser authenticatedUser = createAuthenticatedUser();
        BadRequestException exception = assertThrows(BadRequestException.class, () -> recipeBookController.rename(authenticatedUser.token(), new RecipeBookEndpoint.RenameRequest(string(10), string(10))));
        assertEquals(IDENTIFIER_DOES_NOT_EXIST, exception.getReason());
    }

}