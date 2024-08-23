package se.isai.microservices.core.user.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.isai.microservices.core.user.dto.User;

import java.util.List;

public interface UserService {
    public Flux<User> getUsers();
    public Mono<User> createUser(User userDto);
    public Mono<User> getUserByUsername(String username);
    public Mono<User> getUserByUserId(String userId);
    public Mono<Void> deleteUser(String userId);
}
