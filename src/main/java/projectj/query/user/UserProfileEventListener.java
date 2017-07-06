package projectj.query.user;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projectj.api.user.profile.UserProfileCreatedEvent;

@Component
@Slf4j
public class UserProfileEventListener {

    @Autowired
    private UserProfileViewRepository userProfileViewRepository;

    @EventHandler
    public void on(UserProfileCreatedEvent event) {
        log.info("_Event:{}", event);
        UserProfileView userProfileView = UserProfileView.builder()
                .userId(event.getUserId())
                .nickname(event.getNickname())
                .dob(event.getDob())
                .build();
        userProfileViewRepository.save(userProfileView);
    }
}
