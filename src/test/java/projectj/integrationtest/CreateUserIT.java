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
import projectj.shared.DateUtils;
import projectj.web.v1.UserController;
import projectj.web.v1.UserProfileController;
import projectj.web.v1.dto.UserDto;
import projectj.web.v1.dto.UserProfileDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class,
        UserEventListener.class,
        MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateUserIT {

    @Autowired
    private RestClient restClient;


    @Test
    public void testCreateUser_whenUserDontExistYet() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer.simpson@fox.net");
        expectHttpResponseOk();

        whenQueryUser(userId);
        expectHttpResponseOk();
        expectUserCreated(userId, "homer.simpson@fox.net");
    }

    private void expectHttpResponseOk() {
        restClient.expectHttpResponseOk();
    }


    @Test
    public void testCreateUser_whenUserIdAlreadyUsedExpectNoAddedAgain() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred.flinststone@bedrock.net");
        whenCreateUser(userId, "homer.simpson@fox.net");

        whenQueryUser(userId);
        expectHttpResponseOk();
        expectUserCreated(userId, "fred.flinststone@bedrock.net");
        expectUserNotCreated(userId, "homer.simpson@fox.net");
    }


    @Test
    public void testCreateUser_whenMissingEmailExpectError() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, null);
        expectHttpResponseBadResponse("NotNull.userDto.email");
    }

    private void expectHttpResponseBadResponse(String expectedErrorCode) {
        restClient.expectHttpResponseBadResponse(expectedErrorCode);
    }

    @Test
    public void testCreateUser_whenWrongFormatEmailExpectError() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer.not.email");
        expectHttpResponseBadResponse("Pattern.userDto.email");
    }


    @Test
    public void testCreateUserProfile_whenUserExists() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred.flinststone@bedrock.net");
        expectHttpResponseOk();

        whenCreateUserProfile(userId, "fred", LocalDate.of(1929, 10, 1));
        expectHttpResponseOk();

        whenQueryUserProfile(userId);
        expectHttpResponseOk();
        expectUserProfileCreated(userId, "fred", LocalDate.of(1929, 10, 1));
    }


    @Test
    public void testUpdateUserProfile_whenUserExists() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred.flintstone@bedrock.net");
        expectHttpResponseOk();

        whenCreateUserProfile(userId, "fred", LocalDate.of(1929, 10, 1));
        expectHttpResponseOk();

        whenCreateUserProfile(userId, "fred.flintstone", LocalDate.of(1929, 10, 1));
        expectHttpResponseOk();

        whenQueryUserProfile(userId);
        expectHttpResponseOk();
        expectUserProfileCreated(userId, "fred.flintstone", LocalDate.of(1929, 10, 1));
    }


    @Test
    public void testCreateUserProfile_whenMissingNickname() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred.flintstone@bedrock.net");
        expectHttpResponseOk();

        whenCreateUserProfile(userId, null, LocalDate.of(1929, 10, 1));
        expectHttpResponseBadResponse("NotNull.userProfileDto.nickname");
    }

    @Test
    public void testCreateUserProfile_whenTooLongNickname() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred.flintstone@bedrock.net");
        expectHttpResponseOk();

        whenCreateUserProfile(userId, "A very very long, too long nickname!!!", LocalDate.of(1929, 10, 1));
        expectHttpResponseBadResponse("Size.userProfileDto.nickname");
    }

    @Test
    public void testCreateUserProfile_whenMissingDob() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred.flintstone@bedrock.net");
        expectHttpResponseOk();

        whenCreateUserProfile(userId, "fred", null);
        expectHttpResponseBadResponse("NotNull.userProfileDto.dob");
    }


    private void whenCreateUser(UUID userId, String email) {
        UserDto userDto = UserDto.builder()
                .userId(userId)
                .email(email)
                .build();
        restClient.postForEntity(UserController.USERS_BASE_URL, userDto);
    }

    private void whenCreateUserProfile(UUID userId, String nickname, LocalDate dob) {
        UserProfileDto.UserProfileDtoBuilder userProfileDtoBuilder = UserProfileDto.builder()
                .userId(userId)
                .nickname(nickname);

        if (dob != null) {
            userProfileDtoBuilder = userProfileDtoBuilder.dob(DateUtils.toDate(dob));
        }
        UserProfileDto userProfileDto = userProfileDtoBuilder.build();
        String url = UserProfileController.USER_PROFILES_BASE_URL.replace("{userId}", userId.toString());
        restClient.postForEntity(url, userProfileDto);
    }

    private void whenQueryUser(UUID userId) {
        String url = UserController.USERS_BASE_URL + UserController.QUERY_URL;
        restClient.getForEntity(url.replace("{userId}", userId.toString()));
    }

    private void whenQueryUserProfile(UUID userId) {
        String url = UserProfileController.USER_PROFILES_BASE_URL.replace("{userId}", userId.toString());
        restClient.getForEntity(url);
    }


    private void expectUserProfileCreated(UUID userId, String nickname, LocalDate dob) {
        UserProfileDto userProfileDto = restClient.getResponseBody(UserProfileDto.class);
        assertEquals(userId, userProfileDto.getUserId());
        assertEquals(nickname, userProfileDto.getNickname());
        assertEquals(DateUtils.toDate(dob), userProfileDto.getDob());

        Date now = new Date();
        Date aMinuteAgo = DateUtils.toDate(LocalDateTime.now().minusMinutes(1));
        assertTrue(now.compareTo(userProfileDto.getCreatedDate()) >= 0);
        assertTrue(aMinuteAgo.compareTo(userProfileDto.getCreatedDate()) <= 0);
        assertTrue(now.compareTo(userProfileDto.getLastModifiedDate()) >= 0);
        assertTrue(userProfileDto.getCreatedDate().compareTo(userProfileDto.getLastModifiedDate()) <= 0);
    }

    private void expectUserCreated(UUID userId, String email) {
        UserDto userDto = restClient.getResponseBody(UserDto.class);
        assertEquals(userId, userDto.getUserId());
        assertEquals(email, userDto.getEmail());

        Date now = new Date();
        Date aMinuteAgo = DateUtils.toDate(LocalDateTime.now().minusMinutes(1));
        assertTrue(now.compareTo(userDto.getCreatedDate()) >= 0);
        assertTrue(aMinuteAgo.compareTo(userDto.getCreatedDate()) <= 0);
    }

    private void expectUserNotCreated(UUID userId, String email) {
        UserDto userDto = restClient.getResponseBody(UserDto.class);
        assertEquals(userId, userDto.getUserId());
        assertNotEquals(email, userDto.getEmail());
    }


}
