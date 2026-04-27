package com.example.catalog.dto.product;

import java.math.BigDecimal;

public record ProductSearchCriteria(
        String query,
        Long categoryId,
        Long sellerId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean inStock,
        Boolean active
) {
}
