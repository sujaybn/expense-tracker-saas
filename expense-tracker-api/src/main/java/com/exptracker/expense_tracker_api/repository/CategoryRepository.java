package com.exptracker.expense_tracker_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exptracker.expense_tracker_api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    List<Category> findByTenantId(Long tenantId);
}
