package com.driveden.app.domain.deviceTokens.dto;

import java.time.LocalDateTime;

import com.driveden.app.domain.deviceTokens.model.DevicePlatform;

public record DeviceTokenResponseDTO(
        Long id,
        Long userId,
        DevicePlatform platform,
        String deviceName,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
