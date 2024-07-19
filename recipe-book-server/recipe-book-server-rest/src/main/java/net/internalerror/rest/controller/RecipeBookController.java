package net.internalerror.rest.controller;

import lombok.RequiredArgsConstructor;
import net.internalerror.Messages;
import net.internalerror.enums.RecipeBookMemberRole;
import net.internalerror.exception.BadRequestException;
import net.internalerror.repository.RecipeBookMemberRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.rest.endpoint.RecipeBookEndpoint;
import net.internalerror.rest.service.RecipeBookService;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.RecipeBook;
import net.internalerror.tables.records.RecipeBookMember;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.internalerror.Messages.*;

@RestController
@RequestMapping("/recipe-book")
@RequiredArgsConstructor
public class RecipeBookController implements RecipeBookEndpoint {

    private final JwtService jwtService;

    private final RecipeBookService service;

    private final RecipeBookRepository recipeBookRepository;

    private final RecipeBookMemberRepository recipeBookMemberRepository;

    @Override
    public ResponseEntity<CreateResponse> create(String token, CreateRequest request) {
        return service.create(token, request);
    }

    @Override
    public void rename(String token, RenameRequest request) {
        User user = jwtService.extractUser(token);

        if (!recipeBookRepository.existsByIdentifier(request.identifier())) {
            throw new BadRequestException(IDENTIFIER_DOES_NOT_EXIST);
        }

        RecipeBook recipeBook = recipeBookRepository.getByIdentifier(request.identifier());
        if(!recipeBookMemberRepository.existsByRecipeBookIdAndUserId(recipeBook.getId(), user.getId())){
            throw new BadRequestException(USER_IS_NOT_A_MEMBER_OF_THE_RECIPE_BOOK);
        }

        RecipeBookMember membership = recipeBookMemberRepository.getByRecipeBookIdAndUserId(recipeBook.getId(), user.getId());
        if (membership.getRole() != RecipeBookMemberRole.OWNER){
            throw new BadRequestException(USER_IS_NOT_THE_OWNER_OF_THE_RECIPE_BOOK);
        }

        service.rename(token, request);
    }
}
