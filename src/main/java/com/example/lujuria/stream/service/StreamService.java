package com.example.lujuria.stream.service;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.catalog.entity.Category;
import com.example.lujuria.catalog.entity.CategoryType;
import com.example.lujuria.catalog.entity.Tag;
import com.example.lujuria.catalog.repository.CategoryRepository;
import com.example.lujuria.catalog.repository.TagRepository;
import com.example.lujuria.common.ResourceNotFoundException;
import com.example.lujuria.creator.entity.CreatorProfile;
import com.example.lujuria.creator.service.CreatorService;
import com.example.lujuria.stream.dto.LiveStreamRequest;
import com.example.lujuria.stream.dto.LiveStreamResponse;
import com.example.lujuria.stream.entity.LiveStream;
import com.example.lujuria.stream.entity.StreamStatus;
import com.example.lujuria.stream.repository.LiveStreamRepository;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StreamService {

    private final LiveStreamRepository liveStreamRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CreatorService creatorService;

    public StreamService(
        LiveStreamRepository liveStreamRepository,
        CategoryRepository categoryRepository,
        TagRepository tagRepository,
        CreatorService creatorService
    ) {
        this.liveStreamRepository = liveStreamRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.creatorService = creatorService;
    }

    @Transactional(readOnly = true)
    public List<LiveStreamResponse> listStreams(String country, String category, String tag, StreamStatus status) {
        Specification<LiveStream> specification = Specification.allOf(
            LiveStreamSpecifications.hasCountry(country),
            LiveStreamSpecifications.hasCategorySlug(category),
            LiveStreamSpecifications.hasTagSlug(tag),
            LiveStreamSpecifications.hasStatus(status)
        );

        return liveStreamRepository.findAll(specification).stream()
            .map(LiveStreamResponse::from)
            .toList();
    }

    @Transactional
    public LiveStreamResponse createStream(AppUserPrincipal principal, LiveStreamRequest request) {
        CreatorProfile creator = creatorService.getRequiredCreatorByUserId(principal.getUserId());
        Category category = categoryRepository.findBySlug(request.categorySlug().trim().toLowerCase(Locale.ROOT))
            .filter(existing -> existing.getType() == CategoryType.STREAM)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria de streaming no encontrada."));

        LiveStream stream = new LiveStream();
        stream.setCreator(creator);
        stream.setCategory(category);
        stream.setTags(resolveTags(request.tagSlugs()));
        stream.setTitle(request.title().trim());
        stream.setDescription(request.description());
        stream.setPlaybackUrl(request.playbackUrl().trim());
        stream.setCoverImageUrl(request.coverImageUrl());
        stream.setAccessPrice(request.accessPrice());
        stream.setStatus(request.status() != null ? request.status() : StreamStatus.SCHEDULED);

        if (stream.getStatus() == StreamStatus.LIVE) {
            stream.setStartedAt(Instant.now());
            creator.setOnline(true);
        }

        LiveStream saved = liveStreamRepository.save(stream);
        return LiveStreamResponse.from(saved);
    }

    @Transactional
    public LiveStreamResponse finishStream(AppUserPrincipal principal, Long streamId) {
        CreatorProfile creator = creatorService.getRequiredCreatorByUserId(principal.getUserId());
        LiveStream stream = liveStreamRepository.findById(streamId)
            .orElseThrow(() -> new ResourceNotFoundException("Transmision no encontrada."));

        if (!stream.getCreator().getId().equals(creator.getId())) {
            throw new ResourceNotFoundException("No puedes cerrar una transmision que no te pertenece.");
        }

        stream.setStatus(StreamStatus.ENDED);
        stream.setEndedAt(Instant.now());
        creator.setOnline(false);
        return LiveStreamResponse.from(stream);
    }

    private Set<Tag> resolveTags(List<String> tagSlugs) {
        if (tagSlugs == null || tagSlugs.isEmpty()) {
            return Set.of();
        }
        return new LinkedHashSet<>(tagRepository.findBySlugIn(
            tagSlugs.stream().map(tag -> tag.trim().toLowerCase(Locale.ROOT)).toList()
        ));
    }
}
