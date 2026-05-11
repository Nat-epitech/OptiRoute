package com.optiroute.backend.auth;

import com.optiroute.backend.auth.dto.LoginRequest;
import com.optiroute.backend.auth.dto.LoginResponse;
import com.optiroute.backend.auth.service.AuthService;
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