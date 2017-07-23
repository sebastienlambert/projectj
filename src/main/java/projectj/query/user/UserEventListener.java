/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.query.user;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projectj.api.user.UserCreatedEvent;
import projectj.api.user.UserUpdatedEvent;

@Component
@Slf4j
public class UserEventListener {

    @Autowired
    private UserViewRepository userViewRepository;


    @EventHandler
    public void on(UserCreatedEvent event) {
        log.info("_Event:{}", event);
        UserView userView = UserView.builder()
                .userId(event.getUserId().toString())
                .email(event.getEmail())
                .build();
        userViewRepository.save(userView);
    }

    @EventHandler
    public void on(UserUpdatedEvent event) {
        log.info("_Event:{}", event);
        UserView userView = userViewRepository.findOne(event.getUserId().toString());
        userView.setEmail(event.getEmail());
        userViewRepository.save(userView);
    }
}
