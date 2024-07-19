package net.internalerror.repository;

import net.internalerror.CrudRepository;
import net.internalerror.enums.RecipeBookMemberState;
import net.internalerror.rest.service.RecipeBookMemberService;
import net.internalerror.tables.records.RecipeBookMember;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static net.internalerror.Tables.RECIPE_BOOK_MEMBER;

@Repository
public class RecipeBookMemberRepository extends CrudRepository<RecipeBookMember, Integer> {

    protected RecipeBookMemberRepository(DSLContext dslContext) {
        super(dslContext, RECIPE_BOOK_MEMBER, RECIPE_BOOK_MEMBER.ID, Integer.class);
    }

    public boolean existsByRecipeBookIdAndUserId(int recipeBookId, int userId){
        return exists(RECIPE_BOOK_MEMBER.RECIPE_BOOK_ID.eq(recipeBookId).and(RECIPE_BOOK_MEMBER.USER_ID.eq(userId)));
    }

    public RecipeBookMember getByRecipeBookIdAndUserId(int recipeBookId, int userId){
        return get(RECIPE_BOOK_MEMBER.RECIPE_BOOK_ID.eq(recipeBookId).and(RECIPE_BOOK_MEMBER.USER_ID.eq(userId)));
    }

    public List<RecipeBookMember> readByRecipeBookIdAndState(int recipeBookId, RecipeBookMemberState state){
        return read(RECIPE_BOOK_MEMBER.RECIPE_BOOK_ID.eq(recipeBookId).and(RECIPE_BOOK_MEMBER.STATE.eq(state)));
    }

}
