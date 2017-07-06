package projectj.web.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.web.v1.dto.UserProfileDto;
import projectj.web.v1.dto.UserProfileMapper;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static projectj.web.v1.PackageConstant.BASE_URL;
import static projectj.web.v1.UserProfileController.USER_PROFILES_BASE_URL;

@RestController
@RequestMapping(value = USER_PROFILES_BASE_URL)
@Api
@Slf4j
public class UserProfileController {
    public static final String USER_PROFILES_BASE_URL = BASE_URL + "/users/{userId}/profiles";

    @Autowired
    private CommandGateway commandGateway;

    private UserProfileMapper userProfileMapper = new UserProfileMapper();


    @ApiOperation("Create a new user profile")
    @RequestMapping(method = POST)
    public void createUserProfile(@RequestBody @Valid UserProfileDto userProfile) {
        log.info("_Controller:createUserProfile:{}", userProfile);
        CreateUserProfileCommand command = userProfileMapper.toCreateUserProfileCommand(userProfile);
        commandGateway.send(command);
    }
}
