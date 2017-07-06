package projectj.web.v1.dto;

import org.junit.Test;
import projectj.api.user.CreateUserCommand;
import projectj.query.user.UserView;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class UserMapperTest {


    @Test
    public void testToCreateUserCommand() {
        UserMapper mapper = new UserMapper();
        UserDto userDto = UserDto.builder()
                .userId(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"))
                .email("fred.flinststone@bedrock.net")
                .build();
        CreateUserCommand command = mapper.toCreateUserCommand(userDto);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", command.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", command.getEmail());
    }


    @Test
    public void testToUserDto() {
        UserMapper mapper = new UserMapper();
        UserView userView = UserView.builder()
                .userId(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"))
                .email("fred.flinststone@bedrock.net")
                .createdDate(new Date(1499347938619L))
                .build();
        UserDto userDto = mapper.toUserDto(userView);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", userDto.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", userDto.getEmail());
        assertEquals(new Date(1499347938619L), userDto.getCreatedDate());
    }

}