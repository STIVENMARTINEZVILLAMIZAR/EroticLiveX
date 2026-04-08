package com.example.lujuria.auth.service;

import com.example.lujuria.auth.dto.AuthResponse;
import com.example.lujuria.auth.dto.LoginRequest;
import com.example.lujuria.auth.dto.RegisterRequest;
import com.example.lujuria.auth.security.JwtService;
import com.example.lujuria.common.BusinessConflictException;
import com.example.lujuria.common.BusinessRuleException;
import com.example.lujuria.creator.entity.CreatorProfile;
import com.example.lujuria.creator.repository.CreatorProfileRepository;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.entity.UserPreference;
import com.example.lujuria.user.entity.UserRole;
import com.example.lujuria.user.repository.AppUserRepository;
import com.example.lujuria.user.repository.UserPreferenceRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final CreatorProfileRepository creatorProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
        AppUserRepository appUserRepository,
        UserPreferenceRepository userPreferenceRepository,
        CreatorProfileRepository creatorProfileRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.creatorProfileRepository = creatorProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (appUserRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessConflictException("Ya existe una cuenta con ese correo.");
        }
        if (!request.adultConfirmed()) {
            throw new BusinessRuleException("Debes confirmar que eres mayor de edad.");
        }
        if (!request.acceptedTerms() || !request.acceptedPrivacyPolicy()) {
            throw new BusinessRuleException("Debes aceptar terminos y politica de privacidad.");
        }
        if (Period.between(request.birthDate(), LocalDate.now()).getYears() < 18) {
            throw new BusinessRuleException("La plataforma requiere usuarios mayores de 18 anos.");
        }
        if (request.creatorRequest() && !StringUtils.hasText(request.creatorDisplayName())) {
            throw new BusinessRuleException("Para crear un perfil de creador debes indicar un nombre publico.");
        }
        if (request.creatorRequest()
            && creatorProfileRepository.existsByDisplayNameIgnoreCase(request.creatorDisplayName().trim())) {
            throw new BusinessConflictException("El nombre publico del creador ya esta en uso.");
        }

        AppUser user = new AppUser();
        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setBirthDate(request.birthDate());
        user.setCountry(request.country().trim());
        user.setCity(StringUtils.hasText(request.city()) ? request.city().trim() : null);
        user.setAdultConfirmed(request.adultConfirmed());
        user.setAcceptedTerms(request.acceptedTerms());
        user.setAcceptedPrivacyPolicy(request.acceptedPrivacyPolicy());
        user.setRoles(defaultRoles(request.creatorRequest()));
        AppUser savedUser = appUserRepository.save(user);

        UserPreference preference = new UserPreference();
        preference.setUser(savedUser);
        preference.setLocale("es");
        userPreferenceRepository.save(preference);
        savedUser.setPreference(preference);

        if (request.creatorRequest()) {
            CreatorProfile creatorProfile = new CreatorProfile();
            creatorProfile.setUser(savedUser);
            creatorProfile.setDisplayName(request.creatorDisplayName().trim());
            creatorProfile.setHeadline(request.creatorHeadline());
            creatorProfileRepository.save(creatorProfile);
        }

        return buildAuthResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(normalizedEmail, request.password())
        );

        AppUser user = appUserRepository.findByEmailIgnoreCase(normalizedEmail)
            .orElseThrow(() -> new BusinessRuleException("Credenciales invalidas."));
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(AppUser user) {
        String token = jwtService.generateToken(user);
        Set<String> roles = user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet());
        return new AuthResponse(token, user.getId(), user.getEmail(), roles, user.getRoles().contains(UserRole.CREATOR));
    }

    private Set<UserRole> defaultRoles(boolean creatorRequest) {
        Set<UserRole> roles = new LinkedHashSet<>();
        roles.add(UserRole.CUSTOMER);
        if (creatorRequest) {
            roles.add(UserRole.CREATOR);
        }
        return roles;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
