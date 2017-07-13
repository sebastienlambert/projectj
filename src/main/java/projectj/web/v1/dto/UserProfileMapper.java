/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.web.v1.dto;


import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.query.user.UserProfileView;
import projectj.shared.DateUtils;

import java.time.Instant;
import java.time.ZoneId;

public class UserProfileMapper {

    public CreateUserProfileCommand toCreateUserProfileCommand(UserProfileDto userProfileDto) {
        return CreateUserProfileCommand.builder()
                .userId(userProfileDto.getUserId())
                .nickname(userProfileDto.getNickname())
                .dob(Instant.ofEpochMilli(userProfileDto.getDob().getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                .build();
    }

    public UserProfileDto toUserProfileDto(UserProfileView userProfileView) {
        return UserProfileDto.builder()
                .userId(userProfileView.getUserId())
                .nickname(userProfileView.getNickname())
                .dob(DateUtils.toDate(userProfileView.getDob()))
                .createdDate(DateUtils.toDate(userProfileView.getCreatedDate()))
                .lastModifiedDate(DateUtils.toDate(userProfileView.getLastModifiedDate()))
                .build();
    }
}
