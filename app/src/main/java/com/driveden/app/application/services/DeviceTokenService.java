package com.driveden.app.application.services;

import org.springframework.stereotype.Service;

import com.driveden.app.domain.deviceTokens.dto.DeviceTokenResponseDTO;
import com.driveden.app.domain.deviceTokens.dto.RegisterDeviceTokenDTO;
import com.driveden.app.domain.deviceTokens.model.UserDeviceTokenDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.UserDeviceTokenMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserDeviceTokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final UserDeviceTokenRepository userDeviceTokenRepository;
    private final UsersService usersService;

    @Transactional
    public DeviceTokenResponseDTO registerDeviceToken(RegisterDeviceTokenDTO registerDeviceTokenDTO, Long userId) {
        usersService.findUserById(userId);

        UserDeviceTokenDomain userDeviceTokenDomain = userDeviceTokenRepository
                .findByFcmToken(registerDeviceTokenDTO.getToken())
                .orElseGet(UserDeviceTokenDomain::new);

        userDeviceTokenDomain.setUserId(userId);
        userDeviceTokenDomain.setFcmToken(registerDeviceTokenDTO.getToken());
        userDeviceTokenDomain.setPlatform(registerDeviceTokenDTO.getPlatform());
        userDeviceTokenDomain.setDeviceName(registerDeviceTokenDTO.getDeviceName());
        userDeviceTokenDomain.setIsActive(true);

        return UserDeviceTokenMapper.toResponseDTO(
                userDeviceTokenRepository.save(userDeviceTokenDomain)
        );
    }

    @Transactional
    public void deactivateDeviceToken(String fcmToken) {
        userDeviceTokenRepository.findByFcmToken(fcmToken)
                .ifPresent(userDeviceTokenDomain -> {
                    userDeviceTokenDomain.setIsActive(false);
                    userDeviceTokenRepository.save(userDeviceTokenDomain);
                });
    }
}
