package net.internalerror.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.internalerror.enums.RecipeBookMemberState;
import net.internalerror.helper.EmailHelper;
import net.internalerror.repository.RecipeBookMemberRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.repository.UserRepository;
import net.internalerror.rest.endpoint.RecipeBookMemberEndpoint;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.RecipeBook;
import net.internalerror.tables.records.User;
import org.springframework.stereotype.Service;

import static net.internalerror.Tables.RECIPE_BOOK_MEMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeBookMemberService implements RecipeBookMemberEndpoint {

    private final JwtService jwtService;

    private final EmailHelper emailHelper;

    private final UserRepository userRepository;

    private final RecipeBookRepository recipeBookRepository;

    private final RecipeBookMemberRepository recipeBookMemberRepository;

    @Override
    public void invite(String token, InviteRequest request) {
        User user = jwtService.extractUser(token);
        RecipeBook recipeBook = recipeBookRepository.getByIdentifier(request.identifier());
        logger.debug("User {} invites {} to the recipe book {}", user.getEmail(), request.email(), recipeBook.getName());

        User invitedUser = userRepository.getByEmail(request.email());

        recipeBookMemberRepository.insert(entity -> entity
                .setCreationUserId(user.getId())
                .setModificationUserId(user.getId())
                .setUserId(invitedUser.getId())
                .setRecipeBookId(recipeBook.getId())
        );

        emailHelper.sendEmail(invitedUser.getEmail(), "Recipe book invitation",
                "You have been invited to the recipe book %s by %s".formatted(recipeBook.getName(), user.getEmail()));
    }

    @Override
    public void join(String token, JoinRequest request) {
        User user = jwtService.extractUser(token);
        RecipeBook recipeBook = recipeBookRepository.getByIdentifier(request.identifier());
        logger.debug("User {} joins the recipe book {}", user.getEmail(), recipeBook.getName());

        recipeBookMemberRepository.update(entity -> entity.setState(RecipeBookMemberState.JOINED).setModificationUserId(user.getId()),
                RECIPE_BOOK_MEMBER.RECIPE_BOOK_ID.eq(recipeBook.getId()).and(RECIPE_BOOK_MEMBER.USER_ID.eq(user.getId())));

        recipeBookMemberRepository.readByRecipeBookIdAndState(recipeBook.getId(), RecipeBookMemberState.JOINED).stream()
                .map(member -> userRepository.get(member.getId()))
                .filter(member -> !member.getEmail().equals(user.getEmail()))
                .forEach(member -> emailHelper.sendEmail(member.getEmail(), "New Member", "User %s has joined the recipe book %s".formatted(user.getEmail(), recipeBook.getName())));
    }


}
