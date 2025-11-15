package com.exptracker.expense_tracker_api.service;

import com.exptracker.expense_tracker_api.dto.CategoryRequest;
import com.exptracker.expense_tracker_api.dto.CategoryResponse;
import com.exptracker.expense_tracker_api.entity.Category;
import com.exptracker.expense_tracker_api.entity.Tenant;
import com.exptracker.expense_tracker_api.repository.CategoryRepository;
import com.exptracker.expense_tracker_api.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TenantRepository tenantRepository;

    // CREATE
    public CategoryResponse createCategory(CategoryRequest request) {
        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .tenant(tenant)
                .build();

        categoryRepository.save(category);
        return mapToCategoryResponse(category);
    }

    // MAPPER
    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .tenantId(category.getTenant().getId())
                .build();
    }

    // GET ALL BY TENANT
    public List<CategoryResponse> getAllCategories(Long tenantId) {
        return categoryRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    // GET BY ID
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return mapToCategoryResponse(category);
    }

    // UPDATE
    public CategoryResponse updateCategory(Long id, CategoryRequest req) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existing.setName(req.getName());
        existing.setDescription(req.getDescription());

        categoryRepository.save(existing);
        return mapToCategoryResponse(existing);
    }

    // DELETE
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
