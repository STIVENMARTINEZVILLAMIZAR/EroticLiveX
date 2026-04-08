package com.example.lujuria.stream.dto;

import com.example.lujuria.stream.entity.LiveStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record LiveStreamResponse(
    Long id,
    Long creatorId,
    String creatorDisplayName,
    String creatorCountry,
    String title,
    String description,
    String category,
    Set<String> tags,
    String playbackUrl,
    String coverImageUrl,
    String status,
    int viewerCount,
    BigDecimal accessPrice,
    Instant startedAt,
    Instant endedAt
) {

    public static LiveStreamResponse from(LiveStream stream) {
        return new LiveStreamResponse(
            stream.getId(),
            stream.getCreator().getId(),
            stream.getCreator().getDisplayName(),
            stream.getCreator().getUser().getCountry(),
            stream.getTitle(),
            stream.getDescription(),
            stream.getCategory() != null ? stream.getCategory().getName() : null,
            stream.getTags().stream().map(tag -> tag.getName()).collect(java.util.stream.Collectors.toSet()),
            stream.getPlaybackUrl(),
            stream.getCoverImageUrl(),
            stream.getStatus().name(),
            stream.getViewerCount(),
            stream.getAccessPrice(),
            stream.getStartedAt(),
            stream.getEndedAt()
        );
    }
}
