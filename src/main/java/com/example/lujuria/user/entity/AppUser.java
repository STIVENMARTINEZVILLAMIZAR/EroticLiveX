package com.example.lujuria.user.entity;

import com.example.lujuria.common.BaseEntity;
import com.example.lujuria.creator.entity.CreatorProfile;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "app_users")
public class AppUser extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 80)
    private String country;

    @Column(length = 100)
    private String city;

    @Column(length = 500)
    private String bio;

    @Column(nullable = false)
    private boolean adultConfirmed;

    @Column(nullable = false)
    private boolean acceptedTerms;

    @Column(nullable = false)
    private boolean acceptedPrivacyPolicy;

    @Column(nullable = false)
    private boolean active = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 40)
    private Set<UserRole> roles = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private CreatorProfile creatorProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserPreference preference;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isAdultConfirmed() {
        return adultConfirmed;
    }

    public void setAdultConfirmed(boolean adultConfirmed) {
        this.adultConfirmed = adultConfirmed;
    }

    public boolean isAcceptedTerms() {
        return acceptedTerms;
    }

    public void setAcceptedTerms(boolean acceptedTerms) {
        this.acceptedTerms = acceptedTerms;
    }

    public boolean isAcceptedPrivacyPolicy() {
        return acceptedPrivacyPolicy;
    }

    public void setAcceptedPrivacyPolicy(boolean acceptedPrivacyPolicy) {
        this.acceptedPrivacyPolicy = acceptedPrivacyPolicy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public CreatorProfile getCreatorProfile() {
        return creatorProfile;
    }

    public void setCreatorProfile(CreatorProfile creatorProfile) {
        this.creatorProfile = creatorProfile;
    }

    public UserPreference getPreference() {
        return preference;
    }

    public void setPreference(UserPreference preference) {
        this.preference = preference;
    }
}
