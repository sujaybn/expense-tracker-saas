package com.exptracker.expense_tracker_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exptracker.expense_tracker_api.dto.UpdateUserRequest;
import com.exptracker.expense_tracker_api.dto.UserResponse;
import com.exptracker.expense_tracker_api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Get all users of across the multi-tenants
    
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    //get all users at tenant-level
    
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TENANT_ADMIN')")
    @GetMapping("/tenant/{tenantId}")
    public List<UserResponse> getUsersOfTenant(@PathVariable Long tenantId) {
        return userService.getUsersOfTenant(tenantId);
    }

    //Who am I?
    @GetMapping("/me")
    public UserResponse getMyProfile(@RequestParam String email) {
        return userService.getMyProfile(email);
    }

   //UPDATE USER
    // @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TENANT_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));
    }
    
    //DELETE A USER, restricting to SUPER ADMIN only
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}