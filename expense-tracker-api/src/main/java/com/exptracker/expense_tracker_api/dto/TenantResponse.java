package com.exptracker.expense_tracker_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantResponse {
    private Long id;
    private String name;
}
