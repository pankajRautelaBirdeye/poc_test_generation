package com.example.poc_test_generation.service;

import com.example.poc_test_generation.model.User;
import com.example.poc_test_generation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public User createUser(User user) {
        return repo.save(user);
    }

    public User getUser(Long id) {
        return repo.findById(id);
    }
}