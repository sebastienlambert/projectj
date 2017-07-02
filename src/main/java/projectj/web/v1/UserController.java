package projectj.web.v1;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectj.api.userprofile.CreateUserProfileCommand;
import projectj.web.v1.dto.UserDto;
import projectj.web.v1.dto.UserMapper;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static projectj.web.v1.UserController.USER_URL;

@RestController
@RequestMapping(value = USER_URL)
@Api
@Slf4j
public class UserController {
    static final String VERSION = "v1";
    static final String BASE_URL = "/api/" + VERSION;
    public static final String USER_URL = BASE_URL + "/users";

    @Autowired
    private CommandGateway commandGateway;

    private UserMapper userMapper = new UserMapper();


    @ApiOperation(value = "Create a new user")
    @RequestMapping(method = POST)
    public void createUser(@RequestBody @Valid UserDto user) {
        log.info("_Controller:createUser:{}", user);
        CreateUserProfileCommand command = userMapper.toCreateUserProfileCommand(user);
        commandGateway.send(command);
    }
}