package com.exptracker.expense_tracker_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exptracker.expense_tracker_api.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

      Optional<Tenant> findByName(String name);

}
