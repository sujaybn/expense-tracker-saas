package com.exptracker.expense_tracker_api.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseResponse {
    private Long id;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private Long categoryId;
    private Long tenantId;
    private Long userId;
}
