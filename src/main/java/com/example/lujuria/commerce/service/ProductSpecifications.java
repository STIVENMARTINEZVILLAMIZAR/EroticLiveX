package com.example.lujuria.commerce.service;

import com.example.lujuria.commerce.entity.Product;
import com.example.lujuria.commerce.entity.ProductType;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> isActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("active"));
    }

    public static Specification<Product> hasCategorySlug(String categorySlug) {
        return (root, query, criteriaBuilder) -> {
            if (categorySlug == null || categorySlug.isBlank()) {
                return null;
            }
            query.distinct(true);
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.join("category").get("slug")),
                categorySlug.trim().toLowerCase()
            );
        };
    }

    public static Specification<Product> hasType(ProductType type) {
        return (root, query, criteriaBuilder) ->
            type == null ? null : criteriaBuilder.equal(root.get("type"), type);
    }
}
