package projectj.web.domain.userprofile;

import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import projectj.web.api.userprofile.CreateUserProfileCommand;
import projectj.web.api.userprofile.UserProfileCreatedEvent;

import java.util.UUID;


public class UserProfileTest {
    private FixtureConfiguration<UserProfile> testFixture;
    private CreateUserProfileCommand commandTemplate = CreateUserProfileCommand.builder()
            .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
            .email("fred.fliststone@bedrock.net")
            .nickname("fred")
            .build();

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(UserProfile.class);
        testFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
    }

    @Test
    public void testCreateUserProfile() {
        CreateUserProfileCommand command = commandTemplate;
        UserProfileCreatedEvent expectedEvent = UserProfileCreatedEvent.builder()
                .userId(UUID.fromString("af07d552-63ee-4e44-9cbc-107767de9f17"))
                .email("fred.fliststone@bedrock.net")
                .nickname("fred")
                .build();
        testFixture.givenNoPriorActivity()
                .when(command)
                .expectEvents(expectedEvent);
    }


    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_missingEmail() {
        CreateUserProfileCommand command = commandTemplate.withEmail(null);
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_wrongEmailFormat() {
        CreateUserProfileCommand command = commandTemplate.withEmail("fred.flinststone@");
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_missingNickname() {
        CreateUserProfileCommand command = commandTemplate.withNickname(null);
        testFixture.givenNoPriorActivity()
                .when(command);
    }

    @Test(expected = JSR303ViolationException.class)
    public void testCreateUserProfile_tooLongNickname() {
        CreateUserProfileCommand command = commandTemplate.withNickname("very very long nickname need to be shorter");
        testFixture.givenNoPriorActivity()
                .when(command);
    }

}