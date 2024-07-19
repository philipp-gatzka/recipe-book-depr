package net.internalerror.repository;

import net.internalerror.CrudRepository;
import net.internalerror.tables.records.RecipeBook;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static net.internalerror.Tables.RECIPE_BOOK;

@Repository
public class RecipeBookRepository extends CrudRepository<RecipeBook, Integer> {

    protected RecipeBookRepository(DSLContext dslContext) {
        super(dslContext, RECIPE_BOOK, RECIPE_BOOK.ID, Integer.class);
    }

}
