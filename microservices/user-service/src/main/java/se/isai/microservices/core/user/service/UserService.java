package se.isai.microservices.core.user.service;

import se.isai.microservices.core.user.dto.User;

import java.util.List;

public interface UserService {
    public List<User> getUsers();
    public User createUser(User userDto);
    public User getUserByUsername(String username) throws Exception;
    public User getUserByUserId(String userId) throws Exception;
    public void deleteUser(String userId);
}
