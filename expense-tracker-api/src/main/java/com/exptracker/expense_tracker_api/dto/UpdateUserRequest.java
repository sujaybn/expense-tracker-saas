package com.exptracker.expense_tracker_api.dto;

import com.exptracker.expense_tracker_api.enums.RoleType;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private RoleType role;
    private Long tenantId;
}
