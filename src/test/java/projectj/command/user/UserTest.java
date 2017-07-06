package projectj.command.user;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import projectj.api.user.CreateUserCommand;
import projectj.api.user.UserCreatedEvent;
import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.api.user.profile.UserProfileCreatedEvent;

import java.time.LocalDate;
import java.util.UUID;


public class UserTest {
    private FixtureConfiguration<User> testFixture;
    private CreateUserCommand createUserCommandTemplate = CreateUserCommand.builder()
            .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
            .email("fred.fliststone@bedrock.net")
            .build();

    private CreateUserProfileCommand createUserProfileCommandTemplate = CreateUserProfileCommand.builder()
            .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
            .nickname("fred")
            .dob(LocalDate.of(1929, 10, 1))
            .build();

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(User.class);
        testFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
    }

    @Test
    public void testCreateUser() {
        CreateUserCommand command = createUserCommandTemplate;
        UserCreatedEvent expectedEvent = UserCreatedEvent.builder()
                .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
                .email("fred.fliststone@bedrock.net")
                .build();
        testFixture.givenNoPriorActivity()
                .when(command)
                .expectEvents(expectedEvent);
    }


    @Test(expected = JSR303ViolationException.class)
    public void testCreateUser_missingEmail() {
        CreateUserCommand command = createUserCommandTemplate.withEmail(null);
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUser_wrongEmailFormat() {
        CreateUserCommand command = createUserCommandTemplate.withEmail("fred.flinststone@");
        testFixture.givenNoPriorActivity()
                .when(command);
    }


    @Test
    public void testCreateUserProfile() {
        CreateUserProfileCommand command = createUserProfileCommandTemplate;
        UUID userId = command.getUserId();
        UserProfileCreatedEvent expectedEvent = UserProfileCreatedEvent.builder()
                .userId(userId)
                .nickname("fred")
                .dob(LocalDate.of(1929, 10, 1))
                .build();
        testFixture.given(UserCreatedEvent.builder()
                .userId(userId)
                .email("fred.flinststone@bedrock.net")
                .build())
                .when(command)
                .expectEvents(expectedEvent);
    }


    @Test
    public void testCreateUserProfile_whenUserDontExistExpectException() {
        CreateUserProfileCommand command = createUserProfileCommandTemplate;
        UserProfileCreatedEvent expectedEvent = UserProfileCreatedEvent.builder()
                .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
                .nickname("fred")
                .dob(LocalDate.of(1929, 10, 1))
                .build();
        testFixture.givenNoPriorActivity()
                .when(command)
                .expectException(AggregateNotFoundException.class);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_missingDob() {
        CreateUserProfileCommand command = createUserProfileCommandTemplate.withDob(null);
        testFixture.given(createUserCommandTemplate)
                .when(command);
    }


    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_missingNickname() {
        CreateUserProfileCommand command = createUserProfileCommandTemplate.withNickname(null);
        testFixture.given(createUserCommandTemplate)
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_tooLongNickname() {
        CreateUserProfileCommand command = createUserProfileCommandTemplate.withNickname("A very very long, too long nickname!!");
        testFixture.given(createUserCommandTemplate)
                .when(command);
    }
}