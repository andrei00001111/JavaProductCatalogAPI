package com.example.catalog.dto.product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        boolean active,
        Long categoryId,
        String categoryName,
        Long sellerId,
        String sellerName
) {
}
