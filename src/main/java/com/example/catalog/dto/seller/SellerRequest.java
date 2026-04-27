package com.example.catalog.dto.seller;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record SellerRequest(
        @NotBlank @Size(max = 80) String externalId,
        @NotBlank @Size(max = 200) String name,
        @DecimalMin("0.00") @DecimalMax("5.00") BigDecimal rating
) {
}
