package com.exptracker.expense_tracker_api.controller;

import com.exptracker.expense_tracker_api.dto.LoginRequest;
import com.exptracker.expense_tracker_api.dto.RegisterRequest;
import com.exptracker.expense_tracker_api.dto.UserResponse;
import com.exptracker.expense_tracker_api.service.AuthService;
import com.exptracker.expense_tracker_api.service.CustomUserDetailsService;
import com.exptracker.expense_tracker_api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws Exception {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password", e);
        }

        // Load UserDetails (Spring Security user object)
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Generate JWT
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token));
    }
}