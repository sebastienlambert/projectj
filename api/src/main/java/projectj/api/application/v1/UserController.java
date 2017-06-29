package projectj.api.application.v1;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import projectj.api.application.v1.dto.UserDto;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static projectj.api.application.v1.PackageConstants.BASE_URL;

@RestController(value = BASE_URL + "/users")
@Api
public class UserController {


    @ApiOperation(value="Create a new user")
    @RequestMapping(method= POST)
    public void createUser(@RequestBody UserDto user) {

    }
}
