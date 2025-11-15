package com.exptracker.expense_tracker_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.exptracker.expense_tracker_api.dto.UpdateUserRequest;
import com.exptracker.expense_tracker_api.dto.UserResponse;
import com.exptracker.expense_tracker_api.entity.Tenant;
import com.exptracker.expense_tracker_api.entity.User;
import com.exptracker.expense_tracker_api.repository.TenantRepository;
import com.exptracker.expense_tracker_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    
    //Who am I?
    public UserResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserResponse(user);
    }

    //Get all users per tenant
    public List<UserResponse> getUsersOfTenant(Long tenantId) {

        return userRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // update users 
   public UserResponse updateUser(Long userId, UpdateUserRequest req) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (req.getName() != null && !req.getName().isBlank()) {
        user.setName(req.getName());
    }

    if (req.getRole() != null) {
        user.setRole(req.getRole());
    }

    if (req.getTenantId() != null) {
        Tenant tenant = tenantRepository.findById(req.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        user.setTenant(tenant);
    }

    User saved = userRepository.save(user);

    return UserResponse.from(saved);
}

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())//.name())
                .tenantId(user.getTenant() != null ? user.getTenant().getId() : null)
                .build();
    }

    //get all users
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    //delete user
    public void deleteUser(Long userId) {

    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    userRepository.delete(user);
}

}