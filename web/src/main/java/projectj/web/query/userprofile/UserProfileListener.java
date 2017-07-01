package projectj.web.query.userprofile;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import projectj.web.api.userprofile.UserProfileCreatedEvent;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class UserProfileListener {

    public static List<UserProfileCreatedEvent> eventList = new ArrayList<>();

    @EventHandler
    public void on(UserProfileCreatedEvent event) {
        log.info("_Event:{}", event);
        eventList.add(event);
    }
}
