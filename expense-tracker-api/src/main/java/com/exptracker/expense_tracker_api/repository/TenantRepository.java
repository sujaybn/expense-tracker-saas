package com.exptracker.expense_tracker_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exptracker.expense_tracker_api.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {}
