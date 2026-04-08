package com.example.lujuria.catalog.repository;

import com.example.lujuria.catalog.entity.Category;
import com.example.lujuria.catalog.entity.CategoryType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    List<Category> findByTypeOrderByNameAsc(CategoryType type);
}
