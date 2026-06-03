package com.driveden.app.domain.deviceTokens.dto;

import com.driveden.app.domain.deviceTokens.model.DevicePlatform;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDeviceTokenDTO {

    @NotBlank(message = "token is required")
    private String token;

    @NotNull(message = "platform is required")
    private DevicePlatform platform;

    @Size(max = 255, message = "deviceName must be at most 255 characters")
    private String deviceName;
}
