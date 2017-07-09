package projectj.web.v1;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projectj.api.user.CreateUserCommand;
import projectj.api.user.UpdateUserCommand;
import projectj.query.user.UserView;
import projectj.query.user.UserViewRepository;
import projectj.web.v1.dto.UserDto;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private CommandGateway commandGateway;

    @Mock
    private UserViewRepository userViewRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .userId(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"))
                .email("fred.flinststone@bedrock.net")
                .build();
        userController.createUser(userDto);
        CreateUserCommand command = getPostedCommand(CreateUserCommand.class);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", command.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", command.getEmail());

    }

    @Test
    public void updateUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .email("fred.flinststone@bedrock.net")
                .build();
        userController.updateUser(UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa"), userDto);
        UpdateUserCommand command = getPostedCommand(UpdateUserCommand.class);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", command.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", command.getEmail());
    }

    @Test
    public void getUser() throws Exception {
        UUID userId = UUID.fromString("e641f7f0-4d33-4bd4-86e9-53beea9d55aa");
        UserView userView = UserView.builder()
                .userId(userId)
                .email("fred.flinststone@bedrock.net")
                .build();
        when(userViewRepository.findOne(userId)).thenReturn(userView);
        UserDto userDto = userController.getUser(userId);
        assertEquals("e641f7f0-4d33-4bd4-86e9-53beea9d55aa", userDto.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", userDto.getEmail());
    }

    private <T> T getPostedCommand(Class<T> clazz) {
        ArgumentCaptor<T> argumentCaptor = ArgumentCaptor.forClass(clazz);
        verify(commandGateway, times(1)).send(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}