package com.example.lujuria.user.dto;

import com.example.lujuria.user.entity.ProfileVisibility;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
    @Size(max = 80) String firstName,
    @Size(max = 80) String lastName,
    @Size(max = 80) String country,
    @Size(max = 100) String city,
    @Size(max = 500) String bio,
    Boolean emailNotifications,
    Boolean platformNotifications,
    Boolean marketingNotifications,
    ProfileVisibility profileVisibility,
    @Size(max = 10) String locale
) {
}
