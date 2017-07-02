package projectj.web.v1.dto;

import org.junit.Test;
import projectj.api.userprofile.CreateUserProfileCommand;

import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class UserMapperTest {


    @Test
    public void testToCreateUserCommand() {
        UserMapper mapper = new UserMapper();
        UserDto userDto = UserDto.builder()
                .userId(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"))
                .email("fred.flinststone@bedrock.net")
                .nickname("fred")
                .build();
        CreateUserProfileCommand command = mapper.toCreateUserProfileCommand(userDto);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", command.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", command.getEmail());
        assertEquals("fred", command.getNickname());
    }

}