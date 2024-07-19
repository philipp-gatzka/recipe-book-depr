package net.internalerror.service;

import net.internalerror.ApiTest;
import net.internalerror.endpoint.RecipeBookEndpoint;
import net.internalerror.tables.records.RecipeBook;
import net.internalerror.tables.records.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static net.internalerror.Tables.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecipeBookServiceTest extends ApiTest {

    @Test
    void givenAuthenticatedUser_whenCreate_thenRecipeBookShouldBeCreated() {
        AuthenticatedUser authenticatedUser = createAuthenticatedUser();

        RecipeBookEndpoint.CreateRequest request = new RecipeBookEndpoint.CreateRequest(string(20));
        ResponseEntity<RecipeBookEndpoint.CreateResponse> response = recipeBookController.create(authenticatedUser.token(), request);

        assertEquals(request.name(), recipeBookRepository.get(RECIPE_BOOK.IDENTIFIER.eq(Objects.requireNonNull(response.getBody()).identifier())).getName());

        RecipeBook recipeBook = recipeBookRepository.get(RECIPE_BOOK.IDENTIFIER.eq(response.getBody().identifier()));
        User user = userRepository.get(USER.EMAIL.eq(authenticatedUser.email()));

        assertTrue(recipeBookMembershipRepository.exists(RECIPE_BOOK_MEMBERSHIP.USER_ID.eq(user.getId()).and(RECIPE_BOOK_MEMBERSHIP.RECIPE_BOOK_ID.eq(recipeBook.getId()))));
    }
}
