package projectj.web.v1.dto;


import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.query.user.UserProfileView;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

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
                .dob(Date.from(userProfileView.getDob().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .createdDate(Date.from(userProfileView.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()))
                .lastModifiedDate(Date.from(userProfileView.getLastModifiedDate().atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }
}
