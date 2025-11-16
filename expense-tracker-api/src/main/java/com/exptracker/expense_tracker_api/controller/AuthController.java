package com.exptracker.expense_tracker_api.controller;

import com.exptracker.expense_tracker_api.dto.LoginRequest;
import com.exptracker.expense_tracker_api.dto.RegisterRequest;
import com.exptracker.expense_tracker_api.dto.UserResponse;
import com.exptracker.expense_tracker_api.entity.User;
import com.exptracker.expense_tracker_api.service.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @CrossOrigin(origins = "http://localhost:3000")

    // @PostMapping("/login")
    // public User login(@RequestBody LoginRequest request) {
    //     return authService.login(request);
    // }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    User user = authService.login(request);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(Map.of("message", "Invalid email or password"));
    }
    return ResponseEntity.ok(user);
}
}
