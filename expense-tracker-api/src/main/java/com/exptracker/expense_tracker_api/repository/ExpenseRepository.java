package com.exptracker.expense_tracker_api.repository;

import com.exptracker.expense_tracker_api.entity.Expense;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("""
        SELECT c.name AS category, SUM(e.amount) AS total
        FROM Expense e
        JOIN e.category c
        WHERE e.tenant.id = :tenantId
        GROUP BY c.name
        """)
    List<Object[]> getTotalExpensesByCategory(@Param("tenantId") Long tenantId);
    
    List<Expense> findByUserId(Long userId);

    List<Expense> findByTenantIdAndCategory_Id(Long tenantId, Long categoryId);

    List<Expense> findByTenantId(Long tenantId);


}
