package projectj.web.v1.dto;

import org.junit.Test;
import projectj.api.user.profile.CreateUserProfileCommand;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class UserProfileMapperTest {

    @Test
    public void testToCreateUserProfileCommand() {
        UserProfileMapper mapper = new UserProfileMapper();
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .userId(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"))
                .nickname("fred")
                .dob(Date.from(LocalDate.of(1929, 10, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        CreateUserProfileCommand command = mapper.toCreateUserProfileCommand(userProfileDto);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", command.getUserId().toString());
        assertEquals("fred", command.getNickname());
        assertEquals(LocalDate.of(1929, 10, 1), command.getDob());
    }

}