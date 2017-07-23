/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.web.v1.dto;


import projectj.api.user.CreateUserCommand;
import projectj.api.user.UpdateUserCommand;
import projectj.query.user.UserView;
import projectj.shared.DateUtils;

import java.util.UUID;

public class UserMapper {

    public CreateUserCommand toCreateUserCommand(UserDto userDto) {
        return CreateUserCommand.builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .build();
    }

    public UpdateUserCommand toUpdateUserCommand(UserDto userDto) {
        return UpdateUserCommand.builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .build();
    }

    public UserDto toUserDto(UserView userView) {
        return UserDto.builder()
                .userId(UUID.fromString(userView.getUserId()))
                .email(userView.getEmail())
                .createdDate(DateUtils.toDate(userView.getCreatedDate()))
                .build();
    }
}
