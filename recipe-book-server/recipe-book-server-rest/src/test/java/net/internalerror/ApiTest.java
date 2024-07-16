package net.internalerror;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import net.internalerror.controller.AuthController;
import net.internalerror.controller.RecipeBookController;
import net.internalerror.controller.UserController;
import net.internalerror.endpoint.AuthEndpoint;
import net.internalerror.repository.RecipeBookMembershipRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.repository.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Objects;
import java.util.UUID;

import static net.internalerror.Tables.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {

    @Autowired
    protected AuthController authController;

    @Autowired
    protected UserController userController;

    @Autowired
    protected RecipeBookController recipeBookController;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RecipeBookRepository recipeBookRepository;

    @Autowired
    protected RecipeBookMembershipRepository recipeBookMembershipRepository;

    @Autowired
    private DSLContext dslContext;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    protected final GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);


    protected String string(int length) {
        String string = UUID.randomUUID().toString();

        if (string.length() < length) {
            string += UUID.randomUUID().toString();
        }

        return string.substring(0, length);
    }

    @SneakyThrows
    protected RegisteredUser createRegisteredUser() {
        assertTrue(greenMail.isRunning(), "GreenMail is not running");
        AuthEndpoint.RegisterRequest request = new AuthEndpoint.RegisterRequest(string(10), string(10), string(10) + "@gmail.com", string(10));
        authController.register(request);
        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        String code = GreenMailUtil.getBody(receivedMessage).substring(19);
        return new RegisteredUser(request.email(), code, request.password());
    }

    protected record RegisteredUser(String email, String code, String password) {

    }

    protected VerifiedUser createVerifiedUser() {
        RegisteredUser registeredUser = createRegisteredUser();

        AuthEndpoint.VerifyEmailRequest request = new AuthEndpoint.VerifyEmailRequest(registeredUser.email(), registeredUser.code());
        authController.verifyEmail(request);

        return new VerifiedUser(registeredUser.email, registeredUser.password);
    }

    protected record VerifiedUser(String email, String password) {

    }

    @SneakyThrows
    protected AuthenticatedUser createAuthenticatedUser() {
        VerifiedUser verifiedUser = createVerifiedUser();
        ResponseEntity<AuthEndpoint.LoginResponse> response = authController.login(new AuthEndpoint.LoginRequest(verifiedUser.email, verifiedUser.password));
        return new AuthenticatedUser(verifiedUser.email, Objects.requireNonNull(response.getBody()).token());
    }

    protected record AuthenticatedUser(String email, String token) {

    }

    @BeforeEach
    @SneakyThrows
    @SuppressWarnings("all")
    public void beforeEach() {

        Connection connection = DriverManager.getConnection("jdbc:h2:mem:recipe_book", "sa", "");

        Queries ddl = dslContext.ddl(DefaultSchema.DEFAULT_SCHEMA);
        for (Query query : ddl) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(query.getSQL());
            }
        }

        greenMail.start();
        greenMail.setUser(mailUsername, mailPassword);
    }

    @AfterEach
    public void dropDatabase() {
        Table<?>[] tables = {RECIPE_BOOK_MEMBERSHIP, RECIPE_BOOK, USER};

        for (Table<?> table : tables) {
            dslContext.dropTable(table).execute();
        }

        greenMail.stop();
    }

    protected record TestUser(String email, String password) {

    }

}
