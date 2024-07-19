package net.internalerror.service;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.RecipeBookMembershipEndpoint;
import net.internalerror.enums.RecipeBookMembershipState;
import net.internalerror.repository.RecipeBookMembershipRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.repository.UserRepository;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.RecipeBook;
import net.internalerror.tables.records.RecipeBookMembership;
import net.internalerror.tables.records.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.internalerror.Tables.*;

@Service
@RequiredArgsConstructor
public class RecipeBookMembershipService implements RecipeBookMembershipEndpoint {

    private final RecipeBookRepository recipeBookRepository;

    private final RecipeBookMembershipRepository recipeBookMembershipRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final EmailService emailService;

    @Override
    public void invite(String token, InviteMemberRequest request) {
        User user = jwtService.extractUser(token);

        User invitedUser = userRepository.get(USER.EMAIL.eq(request.email()));
        RecipeBook recipeBook = recipeBookRepository.get(RECIPE_BOOK.IDENTIFIER.eq(request.identifier()));

        recipeBookMembershipRepository.insert(entity -> entity.setCreationUserId(user.getId()).setModificationUserId(user.getId()).setUserId(invitedUser.getId()).setRecipeBookId(recipeBook.getId()));

        emailService.sendEmail(request.email(), "Recipe Book Invitation", "You have been invited to the recipe book: " + recipeBook.getName());
    }

    @Override
    public ResponseEntity<ListMembershipsResponse> list(String token) {
        User user = jwtService.extractUser(token);

        return ResponseEntity.ok(new ListMembershipsResponse(recipeBookMembershipRepository.read(RECIPE_BOOK_MEMBERSHIP.USER_ID.eq(user.getId())).stream().map(membership -> {
            RecipeBook recipeBook = recipeBookRepository.get(membership.getRecipeBookId());
            return new ListMembershipsResponse.Data(recipeBook.getName(), recipeBook.getIdentifier(), membership.getPermission().getName());
        }).toList()));
    }


}
