package com.example.catalog.dto.seller;

import java.math.BigDecimal;

public record SellerResponse(
        Long id,
        String externalId,
        String name,
        BigDecimal rating
) {
}
