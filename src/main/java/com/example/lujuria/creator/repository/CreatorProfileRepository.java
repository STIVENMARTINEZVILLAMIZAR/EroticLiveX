package com.example.lujuria.creator.repository;

import com.example.lujuria.creator.entity.CreatorProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorProfileRepository extends JpaRepository<CreatorProfile, Long> {

    Optional<CreatorProfile> findByDisplayNameIgnoreCase(String displayName);

    Optional<CreatorProfile> findByUserId(Long userId);

    boolean existsByDisplayNameIgnoreCase(String displayName);

    List<CreatorProfile> findAllByOrderByRatingDescDisplayNameAsc();
}
