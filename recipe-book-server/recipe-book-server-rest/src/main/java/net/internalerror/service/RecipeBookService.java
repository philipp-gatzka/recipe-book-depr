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
    public ResponseEntity<CreateRecipeBookResponse> create(String token, CreateRecipeBookRequest request) {
        User user = jwtService.extractUser(token);

        Integer id = recipeBookRepository.insert(entry -> entry
                .setCreationUserId(user.getId())
                .setModificationUserId(user.getId())
                .setName(request.name())
                .setIdentifier(UUID.randomUUID().toString())
        );

        recipeBookMembershipRepository.insert(entry -> entry
                .setCreationUserId(user.getId())
                .setModificationUserId(user.getId())
                .setRecipeBookId(id)
                .setUserId(user.getId())
                .setState(RecipeBookMembershipState.ACCEPTED)
        );

        return ResponseEntity.ok(new CreateRecipeBookResponse(recipeBookRepository.get(RECIPE_BOOK.ID.eq(id)).getIdentifier()));
    }

}
