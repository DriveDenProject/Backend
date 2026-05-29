package com.driveden.app.infrastructure.out.persistence.entity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.driveden.app.domain.auth.model.AuthProvider;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "email", unique = true, length = 100, nullable = false)
    private String email;

    @Column(name = "pwd")
    private String password;

    @Column(name = "phonenumber", length = 20)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection(targetClass = AuthProvider.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_auth_providers", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 50)
    private Set<AuthProvider> authProviders = new HashSet<>();

    @Column(name = "google_id", unique = true, length = 255)
    private String googleId;

    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

}
