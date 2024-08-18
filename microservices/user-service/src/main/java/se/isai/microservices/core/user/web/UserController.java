package se.isai.microservices.core.user.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.isai.microservices.core.user.dto.CreateUserRequestModel;
import se.isai.microservices.core.user.dto.LoginRequestModel;
import se.isai.microservices.core.user.dto.User;
import se.isai.microservices.core.user.dto.UserResponseModel;
import se.isai.microservices.core.user.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private final UserService userService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@RequestBody LoginRequestModel loginRequest) {
        try {
            User user = userService.getUserByUsername(loginRequest.getUsername());

            if(loginRequest.getPassword().equals(user.getPassword())) {
                UserResponseModel returnValue;
                returnValue = new UserResponseModel(user.getUserId(), user.getUsername());
                return ResponseEntity.status(HttpStatus.OK).body(returnValue);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There was a problem fetching the user");
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }

    @PostMapping
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userModel) {
        User newUser = new User(userModel.getUsername(), userModel.getPassword());

        User createdUser = userService.createUser(newUser);

        UserResponseModel userResponse = new UserResponseModel(createdUser.getUserId(), createdUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
