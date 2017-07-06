package projectj.integrationtest;


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
import projectj.api.user.UserCreatedEvent;
import projectj.integrationtest.config.MockConfig;
import projectj.query.user.UserEventListener;
import projectj.web.v1.UserController;
import projectj.web.v1.dto.UserDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private TestRestTemplate restTemplate;

    private JsonSerializer jsonSerializer = new JsonSerializer();
    private ResponseEntity<String> lastResponse;

    @Test
    public void testCreateUser_whenUserDontExistYet() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer", "homer.simpson@fox.net");
        expectHttpResponseOk();

        whenQueryUser(userId);
        expectHttpResponseOk();
        expectUserCreated(UserCreatedEvent.builder()
                .userId(userId)
                .email("homer.simpson@fox.net")
                .build());
    }


    @Test
    public void testCreateUser_whenUserIdAlreadyUsedExpectNoAddedAgain() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred", "fred.flinststone@bedrock.net");
        whenCreateUser(userId, "homer", "homer.simpson@fox.net");

        whenQueryUser(userId);
        expectHttpResponseOk();
        expectUserCreated(UserCreatedEvent.builder()
                .userId(userId)
                .email("fred.flinststone@bedrock.net")
                .build());
        expectUserNotCreated(UserCreatedEvent.builder()
                .userId(userId)
                .email("homer.simpson@fox.net")
                .build());
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
                .email(email)
                .build();
        lastResponse = restTemplate.postForEntity(UserController.USERS_BASE_URL, userDto, String.class);
    }

    private void whenQueryUser(UUID userId) {
        String url = UserController.USERS_BASE_URL + UserController.QUERY_URL;
        lastResponse = restTemplate.getForEntity(url.replace("{userId}", userId.toString()), String.class);
    }

    private void expectHttpResponseOk() {
        assertEquals(HttpStatus.OK, lastResponse.getStatusCode());
    }

    private void expectHttpResponseBadResponse(String expectedErrorCode) {
        assertEquals(HttpStatus.BAD_REQUEST, lastResponse.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = getResponseBody(Map.class);
        List errors = (List) responseBody.get("errors");
        @SuppressWarnings("unchecked")
        Map<String, Object> firstError = (Map<String, Object>) errors.get(0);
        @SuppressWarnings("unchecked")
        List<String> errorCodes = (List<String>) firstError.get("codes");
        assertTrue(errorCodes.contains(expectedErrorCode));
    }

    private void expectUserCreated(UserCreatedEvent event) {
        UserDto userDto = getResponseBody(UserDto.class);
        assertEquals(event.getEmail(), userDto.getEmail());

        Date now = new Date();
        Date aMinuteAgo = Date.from(LocalDateTime.now().minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        assertTrue(now.compareTo(userDto.getCreatedDate()) >= 0);
        assertTrue(aMinuteAgo.compareTo(userDto.getCreatedDate()) <= 0);
    }

    private void expectUserNotCreated(UserCreatedEvent event) {
        UserDto userDto = getResponseBody(UserDto.class);
        assertNotEquals(event.getEmail(), userDto.getEmail());
    }


    private <T> T getResponseBody(Class<T> clazz) {
        return jsonSerializer.deserialize(lastResponse.getBody(), clazz);
    }

}
