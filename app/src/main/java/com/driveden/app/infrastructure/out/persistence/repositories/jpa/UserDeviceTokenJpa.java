package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.UserDeviceTokenEntity;

public interface UserDeviceTokenJpa extends JpaRepository<UserDeviceTokenEntity, Long> {

    Optional<UserDeviceTokenEntity> findByFcmToken(String fcmToken);

    List<UserDeviceTokenEntity> findByUserIdAndIsActiveTrue(Long userId);
}
