package projectj.query.user;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projectj.api.user.UserCreatedEvent;

@Component
@Slf4j
public class UserEventListener {

    @Autowired
    private UserViewRepository userViewRepository;


    @EventHandler
    public void on(UserCreatedEvent event) {
        log.info("_Event:{}", event);
        UserView userView = UserView.builder()
                .userId(event.getUserId())
                .email(event.getEmail())
                .build();
        userViewRepository.save(userView);
    }
}
