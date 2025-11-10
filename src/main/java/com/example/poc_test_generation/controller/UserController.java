package com.example.poc_test_generation.controller;



import com.example.poc_test_generation.annotation.AutoGenerateTest;
import com.example.poc_test_generation.model.User;
import com.example.poc_test_generation.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/create")
    @AutoGenerateTest("Generate full test")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
