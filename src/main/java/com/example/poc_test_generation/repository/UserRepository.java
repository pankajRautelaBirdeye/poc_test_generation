package com.example.poc_test_generation.repository;

import com.example.poc_test_generation.model.User;


public interface UserRepository {
    User save(User user);

    User findById(Long id);
}