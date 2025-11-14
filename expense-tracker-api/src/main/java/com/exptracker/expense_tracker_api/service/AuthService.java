package com.exptracker.expense_tracker_api.service;

import com.exptracker.expense_tracker_api.dto.LoginRequest;
import com.exptracker.expense_tracker_api.dto.RegisterRequest;
import com.exptracker.expense_tracker_api.entity.User;
import com.exptracker.expense_tracker_api.enums.RoleType;
import com.exptracker.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest req) {
        // User user = User.builder()
        //         .email(req.getEmail())
        //         .password(passwordEncoder.encode(req.getPassword()))
        //         .role(req.getRole() != null ? req.getRole() : RoleType.USER)
        //         .tenantId(req.getTenantId())
        //         .build();

        User user = User.builder()
        .email(req.getEmail())
        .passwordHash(passwordEncoder.encode(req.getPassword()))
        .role(RoleType.USER)
        .build();

    //     User user = User.builder()
    // .email(req.getEmail())
    // .passwordHash(passwordEncoder.encode(req.getPassword()))
    // .role(RoleType.USER)
    // .build();


        return userRepository.save(user);
    }

    public User login(LoginRequest req) {
    return userRepository.findByEmail(req.getEmail())
            .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPasswordHash()))
            .orElse(null);
    }
}
