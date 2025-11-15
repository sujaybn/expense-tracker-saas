package com.exptracker.expense_tracker_api.service;

import com.exptracker.expense_tracker_api.dto.ExpenseRequest;
import com.exptracker.expense_tracker_api.dto.ExpenseResponse;
import com.exptracker.expense_tracker_api.entity.Category;
import com.exptracker.expense_tracker_api.entity.Expense;
import com.exptracker.expense_tracker_api.entity.Tenant;
import com.exptracker.expense_tracker_api.entity.User;
import com.exptracker.expense_tracker_api.repository.CategoryRepository;
import com.exptracker.expense_tracker_api.repository.ExpenseRepository;
import com.exptracker.expense_tracker_api.repository.TenantRepository;
import com.exptracker.expense_tracker_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;


    // ===========================
    // CREATE
    // ===========================
    public ExpenseResponse createExpense(ExpenseRequest req, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Tenant tenant = tenantRepository.findById(req.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Expense expense = Expense.builder()
                .amount(req.getAmount())
                .description(req.getDescription())
                .date(req.getDate())
                .category(category)
                .tenant(tenant)
                .user(user)
                .build();

        Expense saved = expenseRepository.save(expense);

        return mapToExpenseResponse(saved);
    }


    // ===========================
    // MAPPER
    // ===========================
    private ExpenseResponse mapToExpenseResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .date(expense.getDate())
                .categoryId(expense.getCategory().getId())
                .tenantId(expense.getTenant().getId())
                .userId(expense.getUser().getId())
                .build();
    }


    // ===========================
    // GET ALL EXPENSES
    // ===========================
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToExpenseResponse)
                .toList();
    }

    // ===========================
    // GET ALL EXPENSE BY ID
    // ===========================
    public ExpenseResponse getExpenseById(Long id) {
    Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found"));

        return mapToExpenseResponse(expense);
        }
    // ===========================
    // UPDATE EXPENSE BY ID
    // ===========================
        public ExpenseResponse updateExpense(Long id, ExpenseRequest req, String email) {

        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Tenant tenant = tenantRepository.findById(req.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        existing.setAmount(req.getAmount());
        existing.setDescription(req.getDescription());
        existing.setDate(req.getDate());
        existing.setCategory(category);
        existing.setTenant(tenant);
        existing.setUser(user);

        Expense updated = expenseRepository.save(existing);

        return mapToExpenseResponse(updated);
        }
}