package com.optiroute.backend.service;

import com.optiroute.backend.dto.user.CreateUserRequest;
import com.optiroute.backend.dto.user.UserResponse;
import com.optiroute.backend.entity.UserEntity;
import com.optiroute.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse createUser(CreateUserRequest request) {
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        UserEntity savedUser = userRepository.save(user);

        return UserResponse.builder().id(savedUser.getId()).email(savedUser.getEmail()).firstName(savedUser.getFirstName()).lastName(savedUser.getLastName()).build();
    }
}