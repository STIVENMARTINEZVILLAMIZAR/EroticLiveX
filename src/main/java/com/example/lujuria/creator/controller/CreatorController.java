package com.example.lujuria.creator.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.creator.dto.CreatorProfileResponse;
import com.example.lujuria.creator.dto.UpsertCreatorProfileRequest;
import com.example.lujuria.creator.service.CreatorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/creators")
public class CreatorController {

    private final CreatorService creatorService;

    public CreatorController(CreatorService creatorService) {
        this.creatorService = creatorService;
    }

    @GetMapping
    public List<CreatorProfileResponse> listCreators() {
        return creatorService.listCreators();
    }

    @GetMapping("/{creatorId}")
    public CreatorProfileResponse getCreator(@PathVariable Long creatorId) {
        return creatorService.getById(creatorId);
    }

    @PostMapping("/me")
    public CreatorProfileResponse upsertMyProfile(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @Valid @RequestBody UpsertCreatorProfileRequest request
    ) {
        return creatorService.upsertMyProfile(principal, request);
    }
}
