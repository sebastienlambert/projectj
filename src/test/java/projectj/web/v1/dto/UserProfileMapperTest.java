package projectj.web.v1.dto;

import org.junit.Test;
import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.query.user.UserProfileView;
import projectj.shared.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class UserProfileMapperTest {

    @Test
    public void testToCreateUserProfileCommand() {
        UserProfileMapper mapper = new UserProfileMapper();
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .userId(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"))
                .nickname("fred")
                .dob(DateUtils.toDate(LocalDate.of(1929, 10, 1)))
                .build();
        CreateUserProfileCommand command = mapper.toCreateUserProfileCommand(userProfileDto);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", command.getUserId().toString());
        assertEquals("fred", command.getNickname());
        assertEquals(LocalDate.of(1929, 10, 1), command.getDob());
    }

    @Test
    public void testToUserProfileDto() {
        UserProfileMapper mapper = new UserProfileMapper();
        UserProfileView userProfileView = UserProfileView.builder()
                .userId(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"))
                .nickname("fred")
                .dob(LocalDate.of(1929, 10, 1))
                .createdDate(LocalDateTime.of(2017, 7, 8, 1, 8, 10))
                .lastModifiedDate(LocalDateTime.of(2017, 7, 8, 1, 27, 56))
                .build();
        UserProfileDto userProfileDto = mapper.toUserProfileDto(userProfileView);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", userProfileDto.getUserId().toString());
        assertEquals("fred", userProfileDto.getNickname());
        assertEquals(DateUtils.toDate(LocalDate.of(1929, 10, 1)), userProfileDto.getDob());
        assertEquals(DateUtils.toDate(LocalDateTime.of(2017, 7, 8, 1, 8, 10)), userProfileDto.getCreatedDate());
        assertEquals(DateUtils.toDate(LocalDateTime.of(2017, 7, 8, 1, 27, 56)), userProfileDto.getLastModifiedDate());
    }

}