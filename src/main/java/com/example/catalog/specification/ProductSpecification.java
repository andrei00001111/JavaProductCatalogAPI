package com.example.catalog.specification;

import com.example.catalog.dto.product.ProductSearchCriteria;
import com.example.catalog.entity.Product;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> build(ProductSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (criteria.query() != null && !criteria.query().isBlank()) {
                String pattern = "%" + criteria.query().toLowerCase() + "%";
                predicates.add(
                        builder.or(
                                builder.like(builder.lower(root.get("name")), pattern),
                                builder.like(builder.lower(root.get("description")), pattern),
                                builder.like(builder.lower(root.get("sku")), pattern)
                        )
                );
            }

            if (criteria.categoryId() != null) {
                predicates.add(builder.equal(root.get("category").get("id"), criteria.categoryId()));
            }

            if (criteria.sellerId() != null) {
                predicates.add(builder.equal(root.get("seller").get("id"), criteria.sellerId()));
            }

            if (criteria.minPrice() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), criteria.minPrice()));
            }

            if (criteria.maxPrice() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), criteria.maxPrice()));
            }

            if (Boolean.TRUE.equals(criteria.inStock())) {
                predicates.add(builder.greaterThan(root.get("stockQuantity"), 0));
            }

            if (criteria.active() != null) {
                predicates.add(builder.equal(root.get("active"), criteria.active()));
            }

            return builder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
