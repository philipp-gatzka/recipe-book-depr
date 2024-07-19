package net.internalerror.helper;

import lombok.RequiredArgsConstructor;
import net.internalerror.enums.RecipeBookMembershipPermission;
import net.internalerror.repository.RecipeBookMembershipRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.repository.UserRepository;
import net.internalerror.tables.records.RecipeBook;
import net.internalerror.tables.records.RecipeBookMembership;
import net.internalerror.tables.records.User;
import org.springframework.stereotype.Service;

import static net.internalerror.Tables.*;

@Service
@RequiredArgsConstructor
public class DataHelper {

    private final UserRepository userRepository;

    private final RecipeBookRepository recipeBookRepository;

    private final RecipeBookMembershipRepository recipeBookMembershipRepository;

    public boolean userExists(String email) {
        return userRepository.exists(USER.EMAIL.eq(email));
    }

    public boolean userIsVerified(String email) {
        User user = userRepository.get(USER.EMAIL.eq(email));
        return user.getEmailVerificationCode() == null;
    }

    public boolean recipeBookExists(String identifier) {
        return recipeBookRepository.exists(RECIPE_BOOK.IDENTIFIER.eq(identifier));
    }

    public boolean userCanAccessRecipeBook(String identifier, String email) {
        RecipeBook recipeBook = recipeBookRepository.get(RECIPE_BOOK.IDENTIFIER.eq(identifier));
        User user = userRepository.get(USER.EMAIL.eq(email));

        return recipeBookMembershipRepository.exists(RECIPE_BOOK_MEMBERSHIP.RECIPE_BOOK_ID.eq(recipeBook.getId()).and(RECIPE_BOOK_MEMBERSHIP.USER_ID.eq(user.getId())));
    }

    public boolean userHasPermissionOnRecipeBook(String identifier, String email, RecipeBookMembershipPermission permission) {
        RecipeBook recipeBook = recipeBookRepository.get(RECIPE_BOOK.IDENTIFIER.eq(identifier));
        User user = userRepository.get(USER.EMAIL.eq(email));
        RecipeBookMembership membership = recipeBookMembershipRepository.get(RECIPE_BOOK_MEMBERSHIP.RECIPE_BOOK_ID.eq(recipeBook.getId()).and(RECIPE_BOOK_MEMBERSHIP.USER_ID.eq(user.getId())));

        return membership.getPermission() == permission;
    }

}
