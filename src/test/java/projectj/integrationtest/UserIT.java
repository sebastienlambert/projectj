package projectj.integrationtest;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projectj.Application;
import projectj.integrationtest.config.MockConfig;
import projectj.query.user.UserEventListener;

import java.util.UUID;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class,
        UserEventListener.class,
        MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIT {

    @Autowired
    private UserFixture userFixture;


    @Test
    public void testCreateUser_whenUserDontExistYet() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "homer.simpson@fox.net")
                .expectHttpResponseOk();
        userFixture
                .whenQueryUser(userId)
                .expectHttpResponseOk()
                .expectUserCreated(userId, "homer.simpson@fox.net");
    }


    @Test
    public void testCreateUser_whenUserIdAlreadyUsedExpectNoAddedAgain() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "fred.flinststone@bedrock.net")
                .whenCreateUser(userId, "homer.simpson@fox.net")
                .whenQueryUser(userId)
                .expectHttpResponseOk()
                .expectUserCreated(userId, "fred.flinststone@bedrock.net")
                .expectUserNotCreated(userId, "homer.simpson@fox.net");
    }


    @Test
    public void testCreateUser_whenMissingEmailExpectError() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, null)
                .expectHttpResponseBadResponse("NotNull.userDto.email");
    }


    @Test
    public void testCreateUser_whenWrongFormatEmailExpectError() {
        UUID userId = UUID.randomUUID();
        userFixture
                .whenCreateUser(userId, "homer.not.email")
                .expectHttpResponseBadResponse("Pattern.userDto.email");
    }
}
