package com.optiroute.backend.controller;

import com.optiroute.backend.dto.user.UserRequest;
import com.optiroute.backend.dto.user.UserResponse;
import com.optiroute.backend.entity.UserEntity;
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
    public List<UserEntity> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }
}