package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.DeviceTokenService;
import com.driveden.app.domain.auth.dto.AuthenticatedUser;
import com.driveden.app.domain.deviceTokens.dto.DeviceTokenResponseDTO;
import com.driveden.app.domain.deviceTokens.dto.RegisterDeviceTokenDTO;
import com.driveden.app.utils.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/device-tokens")
@Validated
public class DeviceTokensController {

    private final DeviceTokenService deviceTokenService;

    @PostMapping
    public CustomResponse<DeviceTokenResponseDTO> registerDeviceToken(
            @Valid @RequestBody RegisterDeviceTokenDTO registerDeviceTokenDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                deviceTokenService.registerDeviceToken(registerDeviceTokenDTO, authenticatedUser.id()),
                HttpStatus.CREATED,
                "Device token registered successfully"
        );
    }
}
