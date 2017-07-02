package projectj.command.userprofile;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import projectj.api.userprofile.CreateUserProfileCommand;
import projectj.api.userprofile.UserProfileCreatedEvent;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@Slf4j
public class UserProfile {

    @AggregateIdentifier
    private UUID userId;

    private String nickname;
    private String email;

    @SuppressWarnings("usused")
    private UserProfile() {
    }

    @CommandHandler
    public UserProfile(CreateUserProfileCommand command) {
        log.info("_CommandHandler:UserProfile:{}", command);
        apply(UserProfileCreatedEvent.builder()
                .userId(command.getUserId())
                .email(command.getEmail())
                .nickname(command.getNickname())
                .build());
    }


    @EventSourcingHandler
    public void on(UserProfileCreatedEvent event) {
        log.info("_EventListener:UserProfile:{}", event);
        this.userId = event.getUserId();
        this.nickname = event.getNickname();
        this.email = event.getEmail();
    }
}
