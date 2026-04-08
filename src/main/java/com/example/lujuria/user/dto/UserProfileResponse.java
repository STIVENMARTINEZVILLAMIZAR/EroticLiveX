package com.example.lujuria.user.dto;

import com.example.lujuria.creator.entity.CreatorProfile;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.entity.UserPreference;
import java.util.Set;

public record UserProfileResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String country,
    String city,
    String bio,
    Set<String> roles,
    boolean adultConfirmed,
    Long creatorProfileId,
    String creatorDisplayName,
    String profileVisibility,
    boolean emailNotifications,
    boolean platformNotifications,
    boolean marketingNotifications,
    String locale
) {

    public static UserProfileResponse from(AppUser user) {
        CreatorProfile creatorProfile = user.getCreatorProfile();
        UserPreference preference = user.getPreference();

        return new UserProfileResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getCountry(),
            user.getCity(),
            user.getBio(),
            user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()),
            user.isAdultConfirmed(),
            creatorProfile != null ? creatorProfile.getId() : null,
            creatorProfile != null ? creatorProfile.getDisplayName() : null,
            preference != null ? preference.getProfileVisibility().name() : null,
            preference != null && preference.isEmailNotifications(),
            preference != null && preference.isPlatformNotifications(),
            preference != null && preference.isMarketingNotifications(),
            preference != null ? preference.getLocale() : null
        );
    }
}
