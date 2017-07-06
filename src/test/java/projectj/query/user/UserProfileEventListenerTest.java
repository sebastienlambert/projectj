package projectj.query.user;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projectj.api.user.profile.UserProfileCreatedEvent;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class UserProfileEventListenerTest {

    @InjectMocks
    private UserProfileEventListener userProfileEventListener;

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
                .nickname("fred")
                .dob(LocalDate.of(1929, 10, 1))
                .build();
        userProfileEventListener.on(event);
        UserProfileView savedView = getSavedUserProfileView();
        assertEquals("3e36b76e-1038-4e27-a52d-aac589e41d94", savedView.getUserId().toString());
        assertEquals("fred", savedView.getNickname());
        assertEquals(LocalDate.of(1929, 10, 1), savedView.getDob());
    }


    private UserProfileView getSavedUserProfileView() {
        ArgumentCaptor<UserProfileView> argumentCaptor = ArgumentCaptor.forClass(UserProfileView.class);
        verify(userProfileViewRepository, times(1)).save(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}