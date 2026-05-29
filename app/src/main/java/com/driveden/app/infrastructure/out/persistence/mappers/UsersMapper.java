package com.driveden.app.infrastructure.out.persistence.mappers;

import java.time.LocalDateTime;
import java.util.HashSet;

import com.driveden.app.domain.auth.model.AuthProvider;
import com.driveden.app.domain.users.dto.RegisterUserDTO;
import com.driveden.app.domain.users.dto.UserDTO;
import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.entity.UsersEntity;

public class UsersMapper {

    public static Users entitytoDomain(UsersEntity entity) {
        if (entity == null) {
            return null;
        }
        return Users.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .phoneNumber(entity.getPhoneNumber())
                .createdAt(entity.getCreatedAt())
                .authProviders(entity.getAuthProviders() == null ? new HashSet<>() : new HashSet<>(entity.getAuthProviders()))
                .googleId(entity.getGoogleId())
                .profilePicture(entity.getProfilePicture())
                .emailVerified(entity.getEmailVerified())
                .build();
    }

    public static UsersEntity domaintoEntity(Users domain) {
        if (domain == null) {
            return null;
        }
        UsersEntity entity = new UsersEntity();
        entity.setId(domain.getId());
        entity.setUsername(domain.getUsername());
        entity.setEmail(domain.getEmail());
        entity.setPassword(domain.getPassword());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setAuthProviders(domain.getAuthProviders() == null ? new HashSet<>() : new HashSet<>(domain.getAuthProviders()));
        entity.setGoogleId(domain.getGoogleId());
        entity.setProfilePicture(domain.getProfilePicture());
        entity.setEmailVerified(Boolean.TRUE.equals(domain.getEmailVerified()));
        return entity;
    }

    public static UserDTO domaintoDTO(Users domain) {
        if (domain == null) {
            return null;
        }
        return new UserDTO(
                domain.getId(),
                domain.getUsername(),
                domain.getEmail(),
                domain.getPhoneNumber(),
                domain.getCreatedAt().toString()
        );
    }

    public static Users registerDtoToDomain(RegisterUserDTO registerUserDTO){
        if (registerUserDTO == null) {
            return null;
        }
        return Users.builder()
                .username(registerUserDTO.username())
                .email(registerUserDTO.email())
                .password(registerUserDTO.password())
                .phoneNumber(registerUserDTO.phoneNumber())
                .createdAt(LocalDateTime.now())
                .authProviders(new HashSet<>(java.util.Set.of(AuthProvider.LOCAL)))
                .emailVerified(false)
                .build();
    }


}
