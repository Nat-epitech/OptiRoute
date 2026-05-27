package com.optiroute.backend.controller;

import com.optiroute.backend.dto.request.LoginRequest;
import com.optiroute.backend.dto.response.LoginResponse;
import com.optiroute.backend.service.AuthService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}