package net.internalerror.repository;

import net.internalerror.CrudRepository;
import net.internalerror.tables.records.RecipeBookMember;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static net.internalerror.Tables.RECIPE_BOOK_MEMBER;

@Repository
public class RecipeBookMembershipRepository extends CrudRepository<RecipeBookMember, Integer> {

    protected RecipeBookMembershipRepository(DSLContext dslContext) {
        super(dslContext, RECIPE_BOOK_MEMBER, RECIPE_BOOK_MEMBER.ID, Integer.class);
    }

}
