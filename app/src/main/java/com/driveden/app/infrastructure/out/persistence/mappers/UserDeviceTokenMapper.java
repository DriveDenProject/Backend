package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.deviceTokens.dto.DeviceTokenResponseDTO;
import com.driveden.app.domain.deviceTokens.model.UserDeviceTokenDomain;
import com.driveden.app.infrastructure.out.persistence.entity.UserDeviceTokenEntity;

public class UserDeviceTokenMapper {

    public static UserDeviceTokenDomain toDomain(UserDeviceTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UserDeviceTokenDomain(
                entity.getId(),
                entity.getUserId(),
                entity.getFcmToken(),
                entity.getPlatform(),
                entity.getDeviceName(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static UserDeviceTokenEntity toEntity(UserDeviceTokenDomain domain) {
        if (domain == null) {
            return null;
        }

        UserDeviceTokenEntity entity = new UserDeviceTokenEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setFcmToken(domain.getFcmToken());
        entity.setPlatform(domain.getPlatform());
        entity.setDeviceName(domain.getDeviceName());
        entity.setIsActive(domain.getIsActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static DeviceTokenResponseDTO toResponseDTO(UserDeviceTokenDomain domain) {
        if (domain == null) {
            return null;
        }

        return new DeviceTokenResponseDTO(
                domain.getId(),
                domain.getUserId(),
                domain.getPlatform(),
                domain.getDeviceName(),
                domain.getIsActive(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
