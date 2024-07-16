package net.internalerror.repository;

import net.internalerror.CrudRepository;
import net.internalerror.tables.records.RecipeBookMembership;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static net.internalerror.Tables.RECIPE_BOOK_MEMBERSHIP;

@Repository
public class RecipeBookMembershipRepository extends CrudRepository<RecipeBookMembership, Integer> {

    protected RecipeBookMembershipRepository(DSLContext dslContext) {
        super(dslContext, RECIPE_BOOK_MEMBERSHIP, RECIPE_BOOK_MEMBERSHIP.ID, Integer.class);
    }
}
