package com.example.lujuria.stream.service;

import com.example.lujuria.stream.entity.LiveStream;
import com.example.lujuria.stream.entity.StreamStatus;
import org.springframework.data.jpa.domain.Specification;

public final class LiveStreamSpecifications {

    private LiveStreamSpecifications() {
    }

    public static Specification<LiveStream> hasStatus(StreamStatus status) {
        return (root, query, criteriaBuilder) ->
            status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<LiveStream> hasCountry(String country) {
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

    public static Specification<LiveStream> hasCategorySlug(String categorySlug) {
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

    public static Specification<LiveStream> hasTagSlug(String tagSlug) {
        return (root, query, criteriaBuilder) -> {
            if (tagSlug == null || tagSlug.isBlank()) {
                return null;
            }
            query.distinct(true);
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.join("tags").get("slug")),
                tagSlug.trim().toLowerCase()
            );
        };
    }
}
