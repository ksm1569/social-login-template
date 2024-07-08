package com.smsoft.sociallogintemplate.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String name;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Column(nullable = false, unique = true)
    private String uniqueIdentifier;

    @Column(nullable = true)
    private String refreshToken;

    @Column(nullable = true)
    private LocalDateTime refreshTokenExpiryDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String name, String imageUrl, AuthProvider provider, String providerId, String uniqueIdentifier) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.uniqueIdentifier = uniqueIdentifier;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime expiryDate) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryDate = expiryDate;
    }

    public void updateUserInfo(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}