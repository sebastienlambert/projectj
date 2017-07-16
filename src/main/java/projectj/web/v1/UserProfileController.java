/*
 * Copyright (c) 2017.  - Sebastien Lambert - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Sebastien Lambert
 */

package projectj.web.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projectj.api.user.profile.CreateUserProfileCommand;
import projectj.query.user.UserProfileView;
import projectj.query.user.UserProfileViewRepository;
import projectj.web.v1.dto.NotFoundException;
import projectj.web.v1.dto.UserProfileDto;
import projectj.web.v1.dto.UserProfileMapper;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static projectj.web.v1.UserProfileController.USER_PROFILES_BASE_URL;

@RestController
@RequestMapping(value = USER_PROFILES_BASE_URL)
@Api
@Slf4j
public class UserProfileController extends AbstractController {
    public static final String USER_PROFILES_BASE_URL = BASE_URL + "/users/{userId}/profiles";

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private UserProfileViewRepository userProfileViewRepository;

    private UserProfileMapper userProfileMapper = new UserProfileMapper();


    @ApiOperation("Create a new user profile")
    @RequestMapping(method = POST)
    public void createUserProfile(@PathVariable UUID userId, @RequestBody @Valid UserProfileDto userProfile) {
        userProfile = userProfile.withUserId(userId);
        log.info("_Controller:createUserProfile:{}", userProfile);
        CreateUserProfileCommand command = userProfileMapper.toCreateUserProfileCommand(userProfile);
        commandGateway.send(command);
    }

    @ApiOperation("Get user profile")
    @RequestMapping(method = GET)
    @ResponseBody
    public UserProfileDto getUserProfile(@PathVariable UUID userId) {
        log.info("_Controller:getUserProfile:{}", userId);
        UserProfileView userProfileView = userProfileViewRepository.findOne(userId);
        if (userProfileView == null) {
            throw new NotFoundException(String.format("User profile %s not found", userId));
        }
        return userProfileMapper.toUserProfileDto(userProfileView);
    }

}
