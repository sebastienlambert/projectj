package projectj.command.user;

import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import projectj.api.user.CreateUserCommand;
import projectj.api.user.UserCreatedEvent;

import java.util.UUID;


public class UserTest {
    private FixtureConfiguration<User> testFixture;
    private CreateUserCommand commandTemplate = CreateUserCommand.builder()
            .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
            .email("fred.fliststone@bedrock.net")
            .build();

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
    }

    @Test
    public void testCreateUserProfile() {
        CreateUserCommand command = commandTemplate;
        UserCreatedEvent expectedEvent = UserCreatedEvent.builder()
                .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
                .email("fred.fliststone@bedrock.net")
                .build();
        testFixture.givenNoPriorActivity()
                .when(command)
                .expectEvents(expectedEvent);
    }


    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_missingEmail() {
        CreateUserCommand command = commandTemplate.withEmail(null);
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_wrongEmailFormat() {
        CreateUserCommand command = commandTemplate.withEmail("fred.flinststone@");
        testFixture.givenNoPriorActivity()
                .when(command);
    }
}