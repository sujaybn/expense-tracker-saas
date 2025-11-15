package com.exptracker.expense_tracker_api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exptracker.expense_tracker_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);

    List<User> findByTenantId(Long tenantId); 
}
