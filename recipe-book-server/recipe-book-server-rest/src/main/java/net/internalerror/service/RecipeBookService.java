package net.internalerror.service;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.RecipeBookEndpoint;
import net.internalerror.enums.RecipeBookMembershipState;
import net.internalerror.repository.RecipeBookMembershipRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static net.internalerror.Tables.RECIPE_BOOK;

@Service
@RequiredArgsConstructor
public class RecipeBookService implements RecipeBookEndpoint {

    private final JwtService jwtService;

    private final RecipeBookRepository recipeBookRepository;

    private final RecipeBookMembershipRepository recipeBookMembershipRepository;

    @Override
    public ResponseEntity<CreateResponse> create(String token, CreateRequest request) {
        User user = jwtService.extractUser(token);

        Integer id = recipeBookRepository.insert(entry -> entry
                .setCreationUserId(user.getId())
                .setModificationUserId(user.getId())
                .setName(request.name())
                .setIdentifier(UUID.randomUUID().toString()));

        recipeBookMembershipRepository.insert(entry -> entry
                .setCreationUserId(user.getId())
                .setModificationUserId(user.getId())
                .setRecipeBookId(id).setUserId(user.getId())
                .setState(RecipeBookMembershipState.ACCEPTED));

        return ResponseEntity.ok(new CreateResponse(recipeBookRepository.get(RECIPE_BOOK.ID.eq(id)).getIdentifier()));
    }

    @Override
    public void rename(String token, RenameRequest request) {
        User user = jwtService.extractUser(token);
        recipeBookRepository.update(entity -> entity
                .setName(request.name())
                .setModificationUserId(user.getId()), RECIPE_BOOK.IDENTIFIER.eq(request.identifier()));
    }

    @Override
    public void delete(String token, DeleteRecipeBookRequest request) {

        //TODO: Delete any data associated with the recipe book

    }

}
