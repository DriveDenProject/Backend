package com.driveden.app.domain.deviceTokens.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceTokenDomain {

    private Long id;
    private Long userId;
    private String fcmToken;
    private DevicePlatform platform;
    private String deviceName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
