package projectj.integrationtest;


import projectj.shared.DateUtils;
import projectj.web.v1.UserController;
import projectj.web.v1.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserFixture extends AbstractFixture<UserFixture> {

    public UserFixture(RestClient restClient) {
        super(restClient);
    }

    public UserFixture whenQueryUser(UUID userId) {
        String url = UserController.USERS_BASE_URL + UserController.QUERY_URL;
        getRestClient().getForEntity(url.replace("{userId}", userId.toString()));
        return this;
    }

    public UserFixture whenCreateUser(UUID userId, String email) {
        UserDto userDto = UserDto.builder()
                .userId(userId)
                .email(email)
                .build();
        getRestClient().postForEntity(UserController.USERS_BASE_URL, userDto);
        return this;
    }


    public UserFixture expectUserCreated(UUID userId, String email) {
        UserDto userDto = getRestClient().getResponseBody(UserDto.class);
        assertEquals(userId, userDto.getUserId());
        assertEquals(email, userDto.getEmail());

        Date now = new Date();
        Date aMinuteAgo = DateUtils.toDate(LocalDateTime.now().minusMinutes(1));
        assertTrue(now.compareTo(userDto.getCreatedDate()) >= 0);
        assertTrue(aMinuteAgo.compareTo(userDto.getCreatedDate()) <= 0);

        return this;
    }

    public UserFixture expectUserNotCreated(UUID userId, String email) {
        UserDto userDto = getRestClient().getResponseBody(UserDto.class);
        assertEquals(userId, userDto.getUserId());
        assertNotEquals(email, userDto.getEmail());
        return this;
    }

}
