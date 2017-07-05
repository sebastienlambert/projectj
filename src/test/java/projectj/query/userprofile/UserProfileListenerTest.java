package projectj.query.userprofile;


import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projectj.api.userprofile.UserProfileCreatedEvent;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class UserProfileListenerTest {

    @InjectMocks
    private UserProfileListener userProfileListener;

    @Mock
    private UserProfileViewRepository userProfileViewRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testOnUserProfileCreatedEvent() {
        UserProfileCreatedEvent event = UserProfileCreatedEvent.builder()
                .userId(UUID.fromString("3e36b76e-1038-4e27-a52d-aac589e41d94"))
                .email("fred.flinststone@bedrock.net")
                .nickname("fred")
                .build();
        userProfileListener.on(event);
        UserProfileView savedView = getSavedUserProfileView();
        assertEquals("3e36b76e-1038-4e27-a52d-aac589e41d94", savedView.getUserId().toString());
        assertEquals("fred.flinststone@bedrock.net", savedView.getEmail());
        assertEquals("fred", savedView.getNickname());
    }


    private UserProfileView getSavedUserProfileView() {
        ArgumentCaptor<UserProfileView> argumentCaptor = ArgumentCaptor.forClass(UserProfileView.class);
        verify(userProfileViewRepository, times(1)).save(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}