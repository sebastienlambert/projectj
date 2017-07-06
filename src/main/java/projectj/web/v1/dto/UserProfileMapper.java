package projectj.web.v1.dto;


import projectj.api.user.profile.CreateUserProfileCommand;

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
}
