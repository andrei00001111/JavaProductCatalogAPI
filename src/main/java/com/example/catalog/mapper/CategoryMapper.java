package com.example.catalog.mapper;

import com.example.catalog.dto.category.CategoryRequest;
import com.example.catalog.dto.category.CategoryResponse;
import com.example.catalog.entity.Category;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public static void apply(CategoryRequest request, Category category) {
        category.setName(request.name().trim());
        category.setDescription(request.description());
    }
}
