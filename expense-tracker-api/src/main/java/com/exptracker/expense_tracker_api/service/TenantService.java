package com.exptracker.expense_tracker_api.service;

import com.exptracker.expense_tracker_api.dto.TenantRequest;
import com.exptracker.expense_tracker_api.dto.TenantResponse;
import com.exptracker.expense_tracker_api.entity.Tenant;
import com.exptracker.expense_tracker_api.repository.TenantRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantResponse createTenant(TenantRequest request) {

        Tenant tenant = Tenant.builder()
                .name(request.getName())
                .build();

        tenant = tenantRepository.save(tenant);

        return TenantResponse.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .build();
    }

      private TenantResponse mapToTenantResponse(Tenant tenant) {
        return TenantResponse.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .build();
    }

    // GET ALL BY TENANT
    public List<TenantResponse> getAllTenants() {
        return tenantRepository.findAll()
                .stream()
                .map(this::mapToTenantResponse)
                .toList();
    }

    // GET BY ID
    public TenantResponse getTenantById(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        return mapToTenantResponse(tenant);
    }

    // UPDATE BY ID
    public TenantResponse updateTenant(Long id, TenantRequest req) {
        Tenant existing = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        existing.setName(req.getName());
        tenantRepository.save(existing);
        return mapToTenantResponse(existing);
    }

    // DELETE BY ID
    public void deleteTenant(Long id) {
        if (!tenantRepository.existsById(id)) {
            throw new RuntimeException("Tenant not found");
        }
        tenantRepository.deleteById(id);
    }
}
