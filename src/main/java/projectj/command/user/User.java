package projectj.command.user;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import projectj.api.user.CreateUserCommand;
import projectj.api.user.UserCreatedEvent;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@Slf4j
public class User {

    @AggregateIdentifier
    private UUID userId;

    private String email;

    @SuppressWarnings("usused")
    private User() {
    }

    @CommandHandler
    public User(CreateUserCommand command) {
        log.info("_CommandHandler:User:{}", command);
        apply(UserCreatedEvent.builder()
                .userId(command.getUserId())
                .email(command.getEmail())
                .build());
    }


    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        log.info("_EventListener:User:{}", event);
        this.userId = event.getUserId();
        this.email = event.getEmail();
    }
}
