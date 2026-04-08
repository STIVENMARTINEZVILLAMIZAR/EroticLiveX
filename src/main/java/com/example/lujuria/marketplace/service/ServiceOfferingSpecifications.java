package com.example.lujuria.marketplace.service;

import com.example.lujuria.marketplace.entity.ServiceMode;
import com.example.lujuria.marketplace.entity.ServiceOffering;
import org.springframework.data.jpa.domain.Specification;

public final class ServiceOfferingSpecifications {

    private ServiceOfferingSpecifications() {
    }

    public static Specification<ServiceOffering> isActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("active"));
    }

    public static Specification<ServiceOffering> hasCountry(String country) {
        return (root, query, criteriaBuilder) -> {
            if (country == null || country.isBlank()) {
                return null;
            }
            query.distinct(true);
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.join("creator").join("user").get("country")),
                country.trim().toLowerCase()
            );
        };
    }

    public static Specification<ServiceOffering> hasCategorySlug(String categorySlug) {
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

    public static Specification<ServiceOffering> hasMode(ServiceMode mode) {
        return (root, query, criteriaBuilder) ->
            mode == null ? null : criteriaBuilder.equal(root.get("mode"), mode);
    }
}
