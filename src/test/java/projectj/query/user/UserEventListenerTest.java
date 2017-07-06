package projectj.query.user;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projectj.api.user.UserCreatedEvent;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class UserEventListenerTest {

    @InjectMocks
    private UserEventListener userEventListener;

    @Mock
    private UserViewRepository userViewRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testOnUserProfileCreatedEvent() {
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94"))
                .email("fred.flinststone@bedrock.net")
                .build();
        userEventListener.on(event);
        UserView savedView = getSavedUserProfileView();
        assertEquals("3e36b76e-1038-4e27-a52d-aac589e41d94", savedView.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", savedView.getEmail());
    }


    private UserView getSavedUserProfileView() {
        ArgumentCaptor<UserView> argumentCaptor = ArgumentCaptor.forClass(UserView.class);
        verify(userViewRepository, times(1)).save(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}