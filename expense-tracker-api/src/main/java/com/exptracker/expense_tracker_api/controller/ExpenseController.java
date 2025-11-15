package com.exptracker.expense_tracker_api.controller;

import com.exptracker.expense_tracker_api.dto.ExpenseRequest;
import com.exptracker.expense_tracker_api.dto.ExpenseResponse;
import com.exptracker.expense_tracker_api.service.ExpenseService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER','MANAGER','TENANT_ADMIN','SUPER_ADMIN')")
public class ExpenseController {

    private final ExpenseService expenseService;

   @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request,Principal principal) {
    String email = principal.getName();
    return ResponseEntity.ok(expenseService.createExpense(request, email));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpense(@PathVariable Long id) {
    return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request,
            @RequestHeader("email") String email
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

}
