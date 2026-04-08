package com.example.lujuria.creator.service;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.common.BusinessConflictException;
import com.example.lujuria.common.ResourceNotFoundException;
import com.example.lujuria.creator.dto.CreatorProfileResponse;
import com.example.lujuria.creator.dto.UpsertCreatorProfileRequest;
import com.example.lujuria.creator.entity.CreatorProfile;
import com.example.lujuria.creator.repository.CreatorProfileRepository;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.entity.UserRole;
import com.example.lujuria.user.service.UserService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatorService {

    private final CreatorProfileRepository creatorProfileRepository;
    private final UserService userService;

    public CreatorService(CreatorProfileRepository creatorProfileRepository, UserService userService) {
        this.creatorProfileRepository = creatorProfileRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<CreatorProfileResponse> listCreators() {
        return creatorProfileRepository.findAllByOrderByRatingDescDisplayNameAsc()
            .stream()
            .map(CreatorProfileResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public CreatorProfileResponse getById(Long creatorId) {
        return CreatorProfileResponse.from(getRequiredCreator(creatorId));
    }

    @Transactional
    public CreatorProfileResponse upsertMyProfile(AppUserPrincipal principal, UpsertCreatorProfileRequest request) {
        AppUser user = userService.getRequiredUser(principal.getUserId());
        CreatorProfile profile = creatorProfileRepository.findByUserId(user.getId()).orElseGet(CreatorProfile::new);

        if (profile.getId() == null && creatorProfileRepository.existsByDisplayNameIgnoreCase(request.displayName())) {
            throw new BusinessConflictException("El nombre publico del creador ya esta en uso.");
        }

        if (profile.getId() != null && !profile.getDisplayName().equalsIgnoreCase(request.displayName())
            && creatorProfileRepository.existsByDisplayNameIgnoreCase(request.displayName())) {
            throw new BusinessConflictException("El nombre publico del creador ya esta en uso.");
        }

        profile.setUser(user);
        profile.setDisplayName(request.displayName().trim());
        profile.setHeadline(request.headline());
        profile.setDescription(request.description());
        profile.setAvatarUrl(request.avatarUrl());

        user.getRoles().add(UserRole.CREATOR);
        CreatorProfile saved = creatorProfileRepository.save(profile);
        user.setCreatorProfile(saved);
        return CreatorProfileResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public CreatorProfile getRequiredCreatorByUserId(Long userId) {
        return creatorProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("El usuario autenticado no tiene perfil de creador."));
    }

    @Transactional(readOnly = true)
    public CreatorProfile getRequiredCreator(Long creatorId) {
        return creatorProfileRepository.findById(creatorId)
            .orElseThrow(() -> new ResourceNotFoundException("Creador no encontrado."));
    }
}
