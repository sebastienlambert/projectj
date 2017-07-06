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
import projectj.query.user.UserView;
import projectj.query.user.UserViewRepository;
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

    @Autowired
    private UserViewRepository userViewRepository;

    private ResponseEntity<Map> lastResponse;

    @Test
    public void testCreateUser_whenUserDontExistYet() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer", "homer.simpson@fox.net");
        expectHttpResponseOk();
        expectUserProfile(UserCreatedEvent.builder()
                .userId(userId)
                .email("homer.simpson@fox.net")
                .build());
    }


    @Test
    public void testCreateUser_whenUserIdAlreadyUsedExpectNoAddedAgain() {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "fred", "fred.flinststone@bedrock.net");
        whenCreateUser(userId, "homer", "homer.simpson@fox.net");
        expectUserProfile(UserCreatedEvent.builder()
                .userId(userId)
                .email("fred.flinststone@bedrock.net")
                .build());
        expectNoUserProfile(UserCreatedEvent.builder()
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

    private void expectUserProfile(UserCreatedEvent event) {
        UserView userView = userViewRepository.findOne(event.getUserId());
        assertEquals(event.getEmail(), userView.getEmail());

        Date now = new Date();
        Date aMinuteAgo = Date.from(LocalDateTime.now().minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        assertTrue(now.compareTo(userView.getCreatedDate()) >= 0);
        assertTrue(aMinuteAgo.compareTo(userView.getCreatedDate()) <= 0);
    }

    private void expectNoUserProfile(UserCreatedEvent event) {
        UserView userView = userViewRepository.findOne(event.getUserId());
        assertNotEquals(event.getEmail(), userView.getEmail());
    }


}
