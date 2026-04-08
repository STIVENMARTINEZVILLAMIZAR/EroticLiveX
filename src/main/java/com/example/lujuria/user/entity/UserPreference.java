package com.example.lujuria.user.entity;

import com.example.lujuria.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_preferences")
public class UserPreference extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    @Column(nullable = false)
    private boolean emailNotifications = true;

    @Column(nullable = false)
    private boolean platformNotifications = true;

    @Column(nullable = false)
    private boolean marketingNotifications = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProfileVisibility profileVisibility = ProfileVisibility.PUBLIC;

    @Column(nullable = false, length = 10)
    private String locale = "es";

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isPlatformNotifications() {
        return platformNotifications;
    }

    public void setPlatformNotifications(boolean platformNotifications) {
        this.platformNotifications = platformNotifications;
    }

    public boolean isMarketingNotifications() {
        return marketingNotifications;
    }

    public void setMarketingNotifications(boolean marketingNotifications) {
        this.marketingNotifications = marketingNotifications;
    }

    public ProfileVisibility getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(ProfileVisibility profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
