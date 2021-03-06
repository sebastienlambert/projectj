/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.command.user;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import projectj.api.user.CreateUserCommand;
import projectj.api.user.UpdateUserCommand;
import projectj.api.user.UserCreatedEvent;
import projectj.api.user.UserUpdatedEvent;
import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.api.user.profile.UserProfileCreatedEvent;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@Slf4j
public class User {

    private static final String LOGKEY_COMMAND_HANDLER = "_CommandHandler:User:{}";
    private static final String LOGKEY_EVENT_HANDLER = "_EventHandler:User:{}";

    @AggregateIdentifier
    private UUID userId;

    private String email;
    private UserProfile userProfile;


    @SuppressWarnings("usused")
    private User() {
    }

    @CommandHandler
    public User(CreateUserCommand command) {
        log.info(LOGKEY_COMMAND_HANDLER, command);
        apply(UserCreatedEvent.builder()
                .userId(command.getUserId())
                .email(command.getEmail())
                .build());
    }

    @CommandHandler
    public void updateUser(UpdateUserCommand command) {
        log.info(LOGKEY_COMMAND_HANDLER, command);
        apply(UserUpdatedEvent.builder()
                .userId(command.getUserId())
                .email(command.getEmail())
                .build());
    }

    @CommandHandler
    public void setUserProfile(CreateUserProfileCommand userProfileCommand) {
        log.info(LOGKEY_COMMAND_HANDLER, userProfileCommand);
        apply(UserProfileCreatedEvent.builder()
                .userId(userId)
                .nickname(userProfileCommand.getNickname())
                .dob(userProfileCommand.getDob())
                .build());
    }


    @EventSourcingHandler
    public void on(UserCreatedEvent event) {
        log.info(LOGKEY_EVENT_HANDLER, event);
        this.userId = event.getUserId();
        this.email = event.getEmail();
    }

    @EventSourcingHandler
    public void on(UserUpdatedEvent event) {
        log.info(LOGKEY_EVENT_HANDLER, event);
        this.userId = event.getUserId();
        this.email = event.getEmail();
    }

    @EventSourcingHandler
    public void on(UserProfileCreatedEvent event) {
        log.info(LOGKEY_EVENT_HANDLER, event);
        this.userProfile = UserProfile.builder()
                .nickname(event.getNickname())
                .dob(event.getDob())
                .build();
    }
}
