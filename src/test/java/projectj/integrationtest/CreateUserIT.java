package projectj.integrationtest;


import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projectj.Application;
import projectj.api.userprofile.UserProfileCreatedEvent;
import projectj.integrationtest.config.MockConfig;
import projectj.query.userprofile.UserProfileListener;
import projectj.web.v1.UserController;
import projectj.web.v1.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class,
        UserProfileListener.class,
        MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateUserIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Datastore datastore;

    private ResponseEntity<Map> lastResponse;

    @Test
    public void testCreateUser_whenUserDontExistYet() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer", "homer.simpson@fox.net");
        expectHttpResponseOk();
        expectUserProfile(UserProfileCreatedEvent.builder()
                .userId(userId)
                .nickname("homer")
                .email("homer.simpson@fox.net")
                .build());
    }


    @Test
    public void testCreateUser_whenUserIdAlreadyUsedExpectNoAddedAgain() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred", "fred.flinststone@bedrock.net");
        whenCreateUser(userId, "homer", "homer.simpson@fox.net");
        expectUserProfile(UserProfileCreatedEvent.builder()
                .userId(userId)
                .nickname("fred")
                .email("fred.flinststone@bedrock.net")
                .build());
        expectNoUserProfile(UserProfileCreatedEvent.builder()
                .userId(userId)
                .nickname("homer")
                .email("homer.simpson@fox.net")
                .build());
    }

    @Test
    public void testCreateUser_whenMissingNicknameExpectError() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, null, "homer.simpson@fox.net");
        expectHttpResponseBadResponse("NotNull.userDto.nickname");
    }

    @Test
    public void testCreateUser_whenNicknameTooLongExpectError() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "a very very long, too long nick name!!!!", "homer.simpson@fox.net");
        expectHttpResponseBadResponse("Size.userDto.nickname");
    }

    @Test
    public void testCreateUser_whenMissingEmailExpectError() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer", null);
        expectHttpResponseBadResponse("NotNull.userDto.email");
    }

    @Test
    public void testCreateUser_whenWrongFormatEmailExpectError() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer", "homer.not.email");
        expectHttpResponseBadResponse("Pattern.userDto.email");
    }


    private void whenCreateUser(UUID userId, String nickname, String email) {
        UserDto userDto = UserDto.builder()
                .userId(userId)
                .nickname(nickname)
                .email(email)
                .build();
        lastResponse = restTemplate.postForEntity(UserController.USER_URL, userDto, Map.class);
    }

    private void expectHttpResponseOk() {
        assertEquals(HttpStatus.OK, lastResponse.getStatusCode());
    }

    private void expectHttpResponseBadResponse(String errorCode) {
        assertEquals(HttpStatus.BAD_REQUEST, lastResponse.getStatusCode());
        List errors = (List) lastResponse.getBody().get("errors");
        @SuppressWarnings("unchecked")
        Map<String, Object> firstError = (Map<String, Object>) errors.get(0);
        @SuppressWarnings("unchecked")
        List<String> errorCodes = (List<String>) firstError.get("codes");
        assertTrue(errorCodes.contains(errorCode));
    }

    private void expectUserProfile(UserProfileCreatedEvent event) {
        Key key = datastore.newKeyFactory().setKind("UserProfileView").newKey(event.getUserId().toString());
        Entity entity = datastore.get(key);
        assertEquals(event.getNickname(), entity.getString("nickname"));
        assertEquals(event.getEmail(), entity.getString("email"));
    }

    private void expectNoUserProfile(UserProfileCreatedEvent event) {
        Key key = datastore.newKeyFactory().setKind("UserProfileView").newKey(event.getUserId().toString());
        Entity entity = datastore.get(key);
        assertNotEquals(event.getNickname(), entity.getString("nickname"));
        assertNotEquals(event.getEmail(), entity.getString("email"));
    }


}
