package projectj.web.v1.dto;


import projectj.api.userprofile.CreateUserProfileCommand;

public class UserMapper {

    public CreateUserProfileCommand toCreateUserProfileCommand(UserDto userDto) {
        return CreateUserProfileCommand.builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .build();
    }
}
