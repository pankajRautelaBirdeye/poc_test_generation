package com.example.poc_test_generation.repository;

import com.example.poc_test_generation.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepoImpl implements UserRepository {


    public static final List<User> userList = new ArrayList<>();

    @Override
    public User save(User user) {
        user.setId((long) Math.floor(Math.random()));
        userList.add(user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return userList.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }
}
