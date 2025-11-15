package com.exptracker.expense_tracker_api.controller;

import com.exptracker.expense_tracker_api.dto.ExpenseRequest;
import com.exptracker.expense_tracker_api.dto.ExpenseResponse;
import com.exptracker.expense_tracker_api.entity.Expense;
import com.exptracker.expense_tracker_api.service.ExpenseService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

   @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request,Principal principal) {
    String email = principal.getName();
    return ResponseEntity.ok(expenseService.createExpense(request, email));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpense(@PathVariable Long id) {
    return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

}
