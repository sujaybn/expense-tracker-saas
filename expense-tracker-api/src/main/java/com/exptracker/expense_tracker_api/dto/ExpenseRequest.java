package com.exptracker.expense_tracker_api.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ExpenseRequest {
    private Double amount;
    private String description;
    private LocalDateTime date;
    private Long categoryId;
    private Long tenantId;
}