package com.exptracker.expense_tracker_api.service;

import com.exptracker.expense_tracker_api.dto.LoginRequest;
import com.exptracker.expense_tracker_api.dto.RegisterRequest;
import com.exptracker.expense_tracker_api.dto.UserResponse;
import com.exptracker.expense_tracker_api.entity.Tenant;
import com.exptracker.expense_tracker_api.entity.User;
import com.exptracker.expense_tracker_api.enums.RoleType;
import com.exptracker.expense_tracker_api.repository.TenantRepository;
import com.exptracker.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest req) {

        Tenant tenant = tenantRepository.findById(req.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(RoleType.USER)
                .tenant(tenant)
                .build();

        User saved = userRepository.save(user);

        return UserResponse.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .role(saved.getRole())
                // .role(saved.getRole().name())
                .tenantId(saved.getTenant().getId())
                .build();
    }

    public User login(LoginRequest req) {
        return userRepository.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPasswordHash()))
                .orElse(null);
    }
}

