package se.isai.microservices.core.user.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
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
    public Mono<User> getUser(@PathVariable String userId) {
        return userService.getUserByUsername(userId);
    }
}
