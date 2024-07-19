package net.internalerror.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.internalerror.repository.RecipeBookMemberRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.rest.endpoint.RecipeBookEndpoint;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.RecipeBook;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static net.internalerror.Tables.RECIPE_BOOK;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeBookService implements RecipeBookEndpoint {

    private final RecipeBookMemberRepository recipeBookMemberRepository;

    private final RecipeBookRepository recipeBookRepository;

    private final JwtService jwtService;

    @Override
    public ResponseEntity<CreateResponse> create(String token, CreateRequest request) {
        User user = jwtService.extractUser(token);
        logger.debug("Creating new recipe book {} by user {}", request.name(), user.getId());

        String identifier = UUID.randomUUID().toString();

        int recipeBookId = recipeBookRepository.insert(entity -> entity
                .setCreationUserId(user.getId())
                .setModificationUserId(user.getId())
                .setName(request.name())
                .setIdentifier(identifier)
        );

        recipeBookMemberRepository.insert(entity -> entity
                .setCreationUserId(user.getId())
                .setModificationUserId(user.getId())
                .setUserId(user.getId())
                .setRecipeBookId(recipeBookId)
        );

        return ResponseEntity.ok(new CreateResponse(identifier));
    }

    @Override
    public void rename(String token, RenameRequest request) {
        User user = jwtService.extractUser(token);
        RecipeBook recipeBook = recipeBookRepository.getByIdentifier(request.identifier());
        logger.debug("Renaming recipe book {} to {} by {}", recipeBook.getId(), request.name(), user.getId());

        recipeBookRepository.update(entity -> entity.setName(request.identifier()).setModificationUserId(user.getId()),
                RECIPE_BOOK.IDENTIFIER.eq(request.identifier())
        );
    }

}
