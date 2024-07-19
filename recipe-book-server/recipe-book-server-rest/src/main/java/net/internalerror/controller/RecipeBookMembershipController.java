package net.internalerror.controller;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.RecipeBookMembershipEndpoint;
import net.internalerror.enums.RecipeBookMembershipPermission;
import net.internalerror.exception.BadRequestException;
import net.internalerror.helper.DataHelper;
import net.internalerror.security.JwtService;
import net.internalerror.service.RecipeBookMembershipService;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.internalerror.Messages.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe-book-membership")
public class RecipeBookMembershipController implements RecipeBookMembershipEndpoint {

    private final JwtService jwtService;

    private final RecipeBookMembershipService service;

    private final DataHelper dataHelper;

    @Override
    public void invite(String token, InviteMemberRequest request) {
        User user = jwtService.extractUser(token);

        if (!dataHelper.recipeBookExists(request.identifier())) {
            throw new BadRequestException(IDENTIFIER_DOES_NOT_EXIST);
        }

        if (!dataHelper.userCanAccessRecipeBook(request.identifier(), user.getEmail())) {
            throw new BadRequestException(USER_IS_NOT_A_MEMBER_OF_THE_RECIPE_BOOK);
        }

        if (!dataHelper.userHasPermissionOnRecipeBook(request.identifier(), user.getEmail(), RecipeBookMembershipPermission.OWNER)) {
            throw new BadRequestException(USER_IS_NOT_THE_OWNER_OF_THE_RECIPE_BOOK);
        }

        if (!dataHelper.userExists(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_REGISTERED);
        }

        if (!dataHelper.userIsVerified(request.email())) {
            throw new BadRequestException(EMAIL_IS_NOT_VERIFIED);
        }

        if (dataHelper.userCanAccessRecipeBook(request.identifier(), request.email())) {
            throw new BadRequestException(USER_IS_ALREADY_A_MEMBER_OF_RECIPE_BOOK);
        }

        service.invite(token, request);
    }

    @Override
    public ResponseEntity<ListMembershipsResponse> list(String token) {
        return service.list(token);
    }

}
