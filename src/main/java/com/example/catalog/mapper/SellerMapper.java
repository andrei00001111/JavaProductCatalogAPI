package com.example.catalog.mapper;

import com.example.catalog.dto.seller.SellerRequest;
import com.example.catalog.dto.seller.SellerResponse;
import com.example.catalog.entity.Seller;

public final class SellerMapper {

    private SellerMapper() {
    }

    public static SellerResponse toResponse(Seller seller) {
        return new SellerResponse(
                seller.getId(),
                seller.getExternalId(),
                seller.getName(),
                seller.getRating()
        );
    }

    public static void apply(SellerRequest request, Seller seller) {
        seller.setExternalId(request.externalId().trim());
        seller.setName(request.name().trim());
        seller.setRating(request.rating());
    }
}
