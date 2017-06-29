package projectj.command.domain.user;

import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

import projectj.core.domain.user.command.CreateUserCommand;
import projectj.core.domain.user.event.UserCreatedEvent;


public class UserTest {

    private FixtureConfiguration<User> testFixture;
    private CreateUserCommand commandTemplate = CreateUserCommand.builder()
            .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
            .email("fred.fliststone@bedrock.net")
            .nickname("fred")
            .build();

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
    }

    @Test
    public void testCreateUser() {
        CreateUserCommand command = commandTemplate;
        UserCreatedEvent expectedEvent = UserCreatedEvent.builder()
                .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
                .email("fred.fliststone@bedrock.net")
                .nickname("fred")
                .build();
        testFixture.givenNoPriorActivity()
                .when(command)
                .expectEvents(expectedEvent);
    }


    @Test(expected = JSR303ViolationException.class)
    public void testCreateUser_missingEmail() {
        CreateUserCommand command = commandTemplate.withEmail(null);
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUser_wrongEmailFormat() {
        CreateUserCommand command = commandTemplate.withEmail("fred.flinststone@");
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUser_missingNickname() {
        CreateUserCommand command = commandTemplate.withNickname(null);
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUser_tooLongNickname() {
        CreateUserCommand command = commandTemplate.withNickname("very very long nickname need to be shorter");
        testFixture.givenNoPriorActivity()
                .when(command);
    }

}