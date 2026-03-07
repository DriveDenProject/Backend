package com.driveden.app.infrastructure.out.persistence.mappers;

import java.time.LocalDateTime;

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
                .build();
    }


}
