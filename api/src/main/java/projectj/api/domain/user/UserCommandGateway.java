package projectj.api.domain.user;


import projectj.core.domain.user.command.CreateUserCommand;

public interface UserCommandGateway {

    void createUser(CreateUserCommand command);
}
