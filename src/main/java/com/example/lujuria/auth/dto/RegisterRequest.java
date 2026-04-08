package com.example.lujuria.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record RegisterRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email @NotBlank String email,
    @NotBlank @Size(min = 8, max = 72) String password,
    @Past @NotNull LocalDate birthDate,
    @NotBlank String country,
    String city,
    boolean adultConfirmed,
    boolean acceptedTerms,
    boolean acceptedPrivacyPolicy,
    boolean creatorRequest,
    String creatorDisplayName,
    String creatorHeadline
) {
}
