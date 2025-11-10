package com.example.poc_test_generation.repository;

import com.example.poc_test_generation.entity.User;

import java.util.Optional;


public interface UserRepository {
    User save(User user);

    User findById(Long id);
}