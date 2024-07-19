package net.internalerror.repository;

import net.internalerror.CrudRepository;
import net.internalerror.tables.records.User;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static net.internalerror.Tables.USER;

@Repository
public class UserRepository extends CrudRepository<User, Integer> {

    protected UserRepository(DSLContext dslContext) {
        super(dslContext, USER, USER.ID, Integer.class);
    }

    public boolean existsByEmail(String email) {
        return exists(USER.EMAIL.eq(email));
    }

    public User getByEmail(String email) {
        return get(USER.EMAIL.eq(email));
    }

}
