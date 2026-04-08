package com.example.lujuria.creator.dto;

import com.example.lujuria.creator.entity.CreatorProfile;

public record CreatorProfileResponse(
    Long id,
    Long userId,
    String displayName,
    String headline,
    String description,
    String avatarUrl,
    boolean verified,
    boolean online,
    String rating,
    String country,
    String city
) {

    public static CreatorProfileResponse from(CreatorProfile profile) {
        return new CreatorProfileResponse(
            profile.getId(),
            profile.getUser().getId(),
            profile.getDisplayName(),
            profile.getHeadline(),
            profile.getDescription(),
            profile.getAvatarUrl(),
            profile.isVerified(),
            profile.isOnline(),
            profile.getRating().toPlainString(),
            profile.getUser().getCountry(),
            profile.getUser().getCity()
        );
    }
}
