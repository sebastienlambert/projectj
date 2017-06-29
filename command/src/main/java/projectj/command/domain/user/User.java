package projectj.command.domain.user;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import projectj.core.domain.user.command.CreateUserCommand;
import projectj.core.domain.user.event.UserCreatedEvent;


import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class User {

    @AggregateIdentifier
    private UUID userId;

    private String nickname;
    private String email;


    @SuppressWarnings("unused")
    private User() { }


    @CommandHandler
    public User(CreateUserCommand command) {
        apply(UserCreatedEvent.builder()
                .userId(command.getUserId())
                .email(command.getEmail())
                .nickname(command.getNickname())
                .build());
    }



    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        this.userId = event.getUserId();
        this.nickname = event.getNickname();
        this.email = event.getEmail();
    }
}
