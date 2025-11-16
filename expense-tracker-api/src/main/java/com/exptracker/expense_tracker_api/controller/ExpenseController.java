package com.exptracker.expense_tracker_api.controller;

import com.exptracker.expense_tracker_api.dto.ExpenseRequest;
import com.exptracker.expense_tracker_api.dto.ExpenseResponse;
import com.exptracker.expense_tracker_api.entity.User;
import com.exptracker.expense_tracker_api.repository.UserRepository;
import com.exptracker.expense_tracker_api.service.ExpenseService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request, @RequestParam String userId) {
        Long userIdLong = Long.valueOf(userId);
        User user = userRepository.findById(userIdLong)
                    .orElseThrow(() -> new RuntimeException("User not found"));

        String email = user.getEmail();
        return ResponseEntity.ok(expenseService.createExpense(request, email));
    }
    
    @GetMapping
        public ResponseEntity<List<ExpenseResponse>> getAllExpenses(@RequestParam String userId) {
            System.out.println("Printing user: "+userId);
            Long userIdLong = Long.valueOf(userId);

            User user = userRepository.findById(userIdLong)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<ExpenseResponse> expenses;

            switch (user.getRole()) {
                case MANAGER -> expenses = expenseService.getAllExpensesByTenant(user.getTenant().getId());
                case USER -> expenses = expenseService.getAllExpensesByUser(user.getId());
                default -> expenses = expenseService.getAllExpenses();
            }
            return ResponseEntity.ok(expenses);
        }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpense(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request,
            Principal principal
    ) {
        String email = principal.getName();
        return ResponseEntity.ok(expenseService.updateExpense(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}

