package projectj.web.v1;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projectj.api.user.CreateUserCommand;
import projectj.api.user.UpdateUserCommand;
import projectj.query.user.UserView;
import projectj.query.user.UserViewRepository;
import projectj.web.v1.dto.NotFoundException;
import projectj.web.v1.dto.UserDto;
import projectj.web.v1.dto.UserMapper;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static projectj.web.v1.PackageConstant.BASE_URL;
import static projectj.web.v1.UserController.USERS_BASE_URL;

@RestController
@RequestMapping(value = USERS_BASE_URL)
@Api
@Slf4j
public class UserController {
    public static final String USERS_BASE_URL = BASE_URL + "/users";
    public static final String USER_ID_URL = "/{userId}";


    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private UserViewRepository userViewRepository;

    private UserMapper userMapper = new UserMapper();


    @ApiOperation("Create a new user")
    @RequestMapping(method = POST)
    public void createUser(@RequestBody @Valid UserDto user) {
        log.info("_Controller:createUser:{}", user);
        CreateUserCommand command = userMapper.toCreateUserCommand(user);
        commandGateway.send(command);
    }

    @ApiOperation("Update a user")
    @RequestMapping(value = USER_ID_URL, method = PUT)
    public void updateUser(@PathVariable UUID userId, @RequestBody @Valid UserDto user) {
        user = user.withUserId(userId);
        log.info("_Controller:updateUser:{}", user);
        UpdateUserCommand command = userMapper.toUpdateUserCommand(user);
        commandGateway.send(command);
    }

    @ApiOperation("Get a specific user profile")
    @RequestMapping(value = USER_ID_URL, method = GET)
    @ResponseBody
    public UserDto getUser(@PathVariable UUID userId) {
        log.info("_Controller:getUser:{}", userId);
        // TODO: should create a UserViewService, move NotFoundException in domain and do this logic there
        UserView user = userViewRepository.findOne(userId);
        if (user == null) {
            throw new NotFoundException("Can't find user " + userId);
        }
        return userMapper.toUserDto(user);
    }

}
