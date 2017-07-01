package projectj.web.interfaces.v1;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectj.web.api.userprofile.CreateUserProfileCommand;
import projectj.web.interfaces.v1.dto.UserDto;
import projectj.web.interfaces.v1.dto.UserMapper;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static projectj.web.interfaces.v1.UserController.BASE_URL;

@RestController
@RequestMapping(value = BASE_URL + "/users")
@Api
public class UserController {
    static final String VERSION = "v1";
    static final String BASE_URL = "/api/" + VERSION;

    @Autowired
    private CommandGateway commandGateway;

    private UserMapper userMapper = new UserMapper();


    @ApiOperation(value = "Create a new user")
    @RequestMapping(method = POST)
    public void createUser(@RequestBody @Valid UserDto user) {
        CreateUserProfileCommand command = userMapper.toCreateUserProfileCommand(user);
        commandGateway.send(command);
    }
}
