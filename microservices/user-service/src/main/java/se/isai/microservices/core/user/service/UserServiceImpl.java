package se.isai.microservices.core.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.isai.microservices.core.user.dto.User;
import se.isai.microservices.core.user.persistence.UserEntity;
import se.isai.microservices.core.user.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        List<UserEntity> userEntities = (List<UserEntity>) userRepository.findAll();
        List<User> users = new ArrayList<>();

        if (userEntities == null || userEntities.isEmpty())
            return new ArrayList<>();

        for(UserEntity entity : userEntities) {
            User newUser = new User(entity.getUserId(), entity.getUsername(), entity.getPassword());
            users.add(newUser);
        }

        return users;
    }

    @Override
    public User createUser(User user) {
        UserEntity userEntity = new UserEntity(user.getUsername(), user.getPassword());

        userEntity.setUserId(UUID.randomUUID().toString());

        userRepository.save(userEntity);

        User newUser = new User(userEntity.getUserId(), userEntity.getUsername(), userEntity.getPassword());

        return newUser;
    }

    @Override
    public User getUserByUsername(String username) throws Exception {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new Exception();
        }

        User newUser = new User(user.getUserId(), user.getUsername(), user.getPassword());

        return newUser;
    }

    @Override
    public User getUserByUserId(String userId) throws Exception {
        UserEntity user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new Exception();
        }

        User newUser = new User(user.getUserId(), user.getUsername(), user.getPassword());

        return newUser;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        userRepository.delete(user);
    }
}
