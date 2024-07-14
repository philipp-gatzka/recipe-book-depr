package net.internalerror;

import lombok.SneakyThrows;
import net.internalerror.controller.AuthController;
import net.internalerror.endpoint.AuthEndpoint;
import net.internalerror.repository.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.UUID;

import static net.internalerror.Tables.USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {

    @Autowired
    protected AuthController authController;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private DSLContext dslContext;


    protected String string(int length) {
        String string = UUID.randomUUID().toString();

        if (string.length() < length) {
            string += UUID.randomUUID().toString();
        }

        return string.substring(0, length);
    }

    @SneakyThrows
    protected TestUser createUser() {
        TestUser user = new TestUser(string(10) + "@gmail.com", string(25));

        authController.register(new AuthEndpoint.RegisterRequest(string(20), string(20), user.email, user.password));

        return user;
    }

    @BeforeEach
    @SneakyThrows
    @SuppressWarnings("all")
    public void beforeEach() {

        Connection connection = DriverManager.getConnection("jdbc:h2:mem:recipe_book", "sa", "");

        Queries ddl = dslContext.ddl(DefaultSchema.DEFAULT_SCHEMA);
        for (Query query : ddl) {
            try(Statement statement = connection.createStatement()){
                statement.execute(query.getSQL());
            }
        }

    }

    @AfterEach
    public void dropDatabase(){
        Table<?>[] tables = {
                USER,
        };

        for (Table<?> table : tables) {
            dslContext.dropTable(table).execute();
        }
    }

    protected record TestUser(String email, String password) {

    }

}
