package com.exptracker.expense_tracker_api.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    // private RoleType role;   
    // private Long tenantId;
}
