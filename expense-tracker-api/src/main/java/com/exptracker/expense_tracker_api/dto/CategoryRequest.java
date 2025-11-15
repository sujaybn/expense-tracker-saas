package com.exptracker.expense_tracker_api.dto;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private Long tenantId;
}
