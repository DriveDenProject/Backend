package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.deviceTokens.model.UserDeviceTokenDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.UserDeviceTokenMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UserDeviceTokenJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDeviceTokenRepository {

    private final UserDeviceTokenJpa userDeviceTokenJpa;

    public UserDeviceTokenDomain save(UserDeviceTokenDomain userDeviceTokenDomain) {
        return UserDeviceTokenMapper.toDomain(
                userDeviceTokenJpa.save(
                        UserDeviceTokenMapper.toEntity(userDeviceTokenDomain)
                )
        );
    }

    public Optional<UserDeviceTokenDomain> findByFcmToken(String fcmToken) {
        return userDeviceTokenJpa.findByFcmToken(fcmToken)
                .map(UserDeviceTokenMapper::toDomain);
    }

    public List<UserDeviceTokenDomain> findActiveByUserId(Long userId) {
        return userDeviceTokenJpa.findByUserIdAndIsActiveTrue(userId).stream()
                .map(UserDeviceTokenMapper::toDomain)
                .toList();
    }
}
