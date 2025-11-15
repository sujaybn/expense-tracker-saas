package com.exptracker.expense_tracker_api.dto;

import com.exptracker.expense_tracker_api.entity.User;
import com.exptracker.expense_tracker_api.enums.RoleType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private RoleType role;
    private Long tenantId;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .tenantId(
                    user.getTenant() != null
                        ? user.getTenant().getId()
                        : null
                )
                .build();
    }
}