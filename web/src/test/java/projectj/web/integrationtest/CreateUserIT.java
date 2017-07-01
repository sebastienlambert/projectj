package projectj.web.integrationtest;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projectj.web.Application;
import projectj.web.api.userprofile.UserProfileCreatedEvent;
import projectj.web.integrationtest.config.MockConfig;
import projectj.web.interfaces.v1.UserController;
import projectj.web.interfaces.v1.dto.UserDto;
import projectj.web.query.userprofile.UserProfileListener;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, UserProfileListener.class, MockConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateUserIT {

    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity lastResponse;


    @Before
    public void setUp() {
        UserProfileListener.eventList.clear();
    }

    @Test
    public void testCreateUser_whenUserDontExistYet() throws InterruptedException {
        UUID userId = UUID.randomUUID();
        whenCreateUser(userId, "homer", "homer.simpson@fox.net");
        expectHttpResponseOk();
        expectUserProfile(UserProfileCreatedEvent.builder()
                .userId(userId)
                .nickname("homer")
                .email("homer.simpson@fox.net")
                .build());
    }


    private void whenCreateUser(UUID userId, String nickname, String email) {
        UserDto userDto = UserDto.builder()
                .userId(userId)
                .nickname(nickname)
                .email(email)
                .build();
        lastResponse = restTemplate.postForEntity(UserController.USER_URL, userDto, Void.class);
    }

    private void expectHttpResponseOk() {
        assertEquals(HttpStatus.OK, lastResponse.getStatusCode());
    }

    private void expectUserProfile(UserProfileCreatedEvent event) {
        assertTrue(UserProfileListener.eventList.contains(event));
    }


}
