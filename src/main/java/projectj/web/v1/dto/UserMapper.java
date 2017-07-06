package projectj.web.v1.dto;


import projectj.api.user.CreateUserCommand;
import projectj.query.user.UserView;

public class UserMapper {

    public CreateUserCommand toCreateUserCommand(UserDto userDto) {
        return CreateUserCommand.builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .build();
    }

    public UserDto toUserDto(UserView userView) {
        return UserDto.builder()
                .userId(userView.getUserId())
                .email(userView.getEmail())
                .createdDate(userView.getCreatedDate())
                .build();
    }
}
