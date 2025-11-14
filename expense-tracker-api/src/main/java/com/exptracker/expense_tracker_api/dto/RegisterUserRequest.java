package com.exptracker.expense_tracker_api.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private Long tenantId;
    private String name;
    private String email;
    private String password;
    private String role;
}
