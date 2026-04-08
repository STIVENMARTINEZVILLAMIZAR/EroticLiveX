package com.example.lujuria.user.service;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.common.ResourceNotFoundException;
import com.example.lujuria.user.dto.UpdateUserProfileRequest;
import com.example.lujuria.user.dto.UserProfileResponse;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.entity.UserPreference;
import com.example.lujuria.user.repository.AppUserRepository;
import com.example.lujuria.user.repository.UserPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    public UserService(AppUserRepository appUserRepository, UserPreferenceRepository userPreferenceRepository) {
        this.appUserRepository = appUserRepository;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentProfile(AppUserPrincipal principal) {
        return UserProfileResponse.from(getRequiredUser(principal.getUserId()));
    }

    @Transactional
    public UserProfileResponse updateCurrentProfile(AppUserPrincipal principal, UpdateUserProfileRequest request) {
        AppUser user = getRequiredUser(principal.getUserId());
        UserPreference preference = userPreferenceRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Preferencias de usuario no encontradas."));

        if (StringUtils.hasText(request.firstName())) {
            user.setFirstName(request.firstName().trim());
        }
        if (StringUtils.hasText(request.lastName())) {
            user.setLastName(request.lastName().trim());
        }
        if (StringUtils.hasText(request.country())) {
            user.setCountry(request.country().trim());
        }
        if (StringUtils.hasText(request.city())) {
            user.setCity(request.city().trim());
        }
        if (request.bio() != null) {
            user.setBio(request.bio().trim());
        }
        if (request.emailNotifications() != null) {
            preference.setEmailNotifications(request.emailNotifications());
        }
        if (request.platformNotifications() != null) {
            preference.setPlatformNotifications(request.platformNotifications());
        }
        if (request.marketingNotifications() != null) {
            preference.setMarketingNotifications(request.marketingNotifications());
        }
        if (request.profileVisibility() != null) {
            preference.setProfileVisibility(request.profileVisibility());
        }
        if (StringUtils.hasText(request.locale())) {
            preference.setLocale(request.locale().trim());
        }

        return UserProfileResponse.from(user);
    }

    @Transactional(readOnly = true)
    public AppUser getRequiredUser(Long userId) {
        return appUserRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));
    }
}
