package se.isai.microservices.core.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.isai.microservices.core.user.dto.User;
import se.isai.microservices.core.user.persistence.UserEntity;
import se.isai.microservices.core.user.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Scheduler jdbcScheduler;

    UserRepository userRepository;


    @Autowired
    public UserServiceImpl(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, UserRepository userRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<User> getUsers() {
        return Mono.fromCallable(() -> {
            List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
            List<User> users = new ArrayList<>();

            for(UserEntity entity : userEntities) {
                User newUser = new User(entity.getUserId(), entity.getUsername(), entity.getPassword());
                users.add(newUser);
            }

            return users;
        })
                .flatMapMany(Flux::fromIterable)
                .log(LOG.getName(), Level.FINE)
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<User> createUser(User user) {
        return Mono.fromCallable(() -> {
            UserEntity userEntity = new UserEntity(user.getUsername(), user.getPassword());

            userEntity.setUserId(UUID.randomUUID().toString());

            userRepository.save(userEntity);

            User newUser = new User(userEntity.getUserId(), userEntity.getUsername(), userEntity.getPassword());

            return newUser;
        }).subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<User> getUserByUsername(String username) {
        return Mono.fromCallable(() -> {
            UserEntity user = userRepository.findByUsername(username);

            if (user == null) {
                throw new Exception();
            }

            User newUser = new User(user.getUserId(), user.getUsername(), user.getPassword());

            return newUser;
        }).subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<User> getUserByUserId(String userId)  {
        return Mono.fromCallable(() -> {
            UserEntity user = userRepository.findByUserId(userId);

            if (user == null) {
                throw new Exception();
            }

            User newUser = new User(user.getUserId(), user.getUsername(), user.getPassword());

            return newUser;
        }).subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        return Mono.fromRunnable(() -> {
            UserEntity user = userRepository.findByUserId(userId);
            userRepository.delete(user);
        }).subscribeOn(jdbcScheduler).then();
    }
}
