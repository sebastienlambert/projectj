package projectj.web.v1.dto;


import projectj.api.user.CreateUserCommand;

public class UserMapper {

    public CreateUserCommand toCreateUserCommand(UserDto userDto) {
        return CreateUserCommand.builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .build();
    }
}
