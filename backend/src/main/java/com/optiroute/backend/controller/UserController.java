package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.UserRequest;
import com.optiroute.backend.dto.response.UserResponse;
import com.optiroute.backend.entity.User;
import com.optiroute.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }
}