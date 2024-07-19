package net.internalerror.repository;

import net.internalerror.CrudRepository;
import net.internalerror.enums.RecipeBookMembershipState;
import net.internalerror.tables.records.RecipeBookMembership;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static net.internalerror.Tables.RECIPE_BOOK_MEMBERSHIP;

@Repository
public class RecipeBookMembershipRepository extends CrudRepository<RecipeBookMembership, Integer> {

    protected RecipeBookMembershipRepository(DSLContext dslContext) {
        super(dslContext, RECIPE_BOOK_MEMBERSHIP, RECIPE_BOOK_MEMBERSHIP.ID, Integer.class);
    }

    public List<RecipeBookMembership> readByRecipeBookId(int recipeBookId) {
        return read(RECIPE_BOOK_MEMBERSHIP.RECIPE_BOOK_ID.eq(recipeBookId));
    }

    public List<RecipeBookMembership> readByRecipeBookIdAndState(int recipeBookId, RecipeBookMembershipState state) {
        return read(RECIPE_BOOK_MEMBERSHIP.RECIPE_BOOK_ID.eq(recipeBookId).and(RECIPE_BOOK_MEMBERSHIP.STATE.eq(state)));
    }

    public List<RecipeBookMembership> readByUserId(int userId) {
        return read(RECIPE_BOOK_MEMBERSHIP.USER_ID.eq(userId));
    }

    public boolean existsByUserIdAndRecipeBookId(int recipeBookId, int userId) {
        return exists(RECIPE_BOOK_MEMBERSHIP.USER_ID.eq(userId).and(RECIPE_BOOK_MEMBERSHIP.RECIPE_BOOK_ID.eq(recipeBookId)));
    }

    public RecipeBookMembership getByUserIdAndRecipeBookId(int recipeBookId, int userId) {
        return get(RECIPE_BOOK_MEMBERSHIP.USER_ID.eq(userId).and(RECIPE_BOOK_MEMBERSHIP.RECIPE_BOOK_ID.eq(recipeBookId)));
    }
}
