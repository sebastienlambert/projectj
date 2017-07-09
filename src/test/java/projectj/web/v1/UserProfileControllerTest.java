package projectj.web.v1;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.query.user.UserProfileView;
import projectj.query.user.UserProfileViewRepository;
import projectj.shared.DateUtils;
import projectj.web.v1.dto.UserProfileDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Sebastien Lambert on 7/8/2017.
 */
public class UserProfileControllerTest {

    @InjectMocks
    private UserProfileController userProfileController;

    @Mock
    private CommandGateway commandGateway;

    @Mock
    private UserProfileViewRepository userProfileViewRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUserProfile() throws Exception {
        UUID userId = UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa");
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .nickname("fred")
                .dob(DateUtils.toDate(LocalDate.of(1929, 10, 1)))
                .build();
        userProfileController.createUserProfile(userId, userProfileDto);
        CreateUserProfileCommand command = getPostedCommand(CreateUserProfileCommand.class);
        assertEquals(userId, command.getUserId());
        assertEquals("fred", command.getNickname());
        assertEquals(LocalDate.of(1929, 10, 1), command.getDob());
    }

    @Test
    public void getUserProfile() throws Exception {
        UUID userId = UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa");
        UserProfileView userProfileView = UserProfileView.builder()
                .userId(userId)
                .nickname("fred")
                .dob(LocalDate.of(1929, 10, 1))
                .createdDate(LocalDateTime.of(2017, 7, 8, 1, 8, 10))
                .lastModifiedDate(LocalDateTime.of(2017, 7, 8, 1, 27, 56))
                .build();
        when(userProfileViewRepository.findOne(userId)).thenReturn(userProfileView);
        UserProfileDto userProfileDto = userProfileController.getUserProfile(userId);
        assertEquals(userId, userProfileDto.getUserId());
        assertEquals("fred", userProfileDto.getNickname());
        assertEquals(DateUtils.toDate(LocalDate.of(1929, 10, 1)), userProfileDto.getDob());
        assertEquals(DateUtils.toDate(LocalDateTime.of(2017, 7, 8, 1, 8, 10)), userProfileDto.getCreatedDate());
        assertEquals(DateUtils.toDate(LocalDateTime.of(2017, 7, 8, 1, 27, 56)), userProfileDto.getLastModifiedDate());
    }


    private <T> T getPostedCommand(Class<T> clazz) {
        ArgumentCaptor<T> argumentCaptor = ArgumentCaptor.forClass(clazz);
        verify(commandGateway, times(1)).send(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }
}