package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.SubscriptionUsageEntity;

public interface SubscriptionUsageJpa extends JpaRepository<SubscriptionUsageEntity, Long> {

    Optional<SubscriptionUsageEntity> findBySubscriptionIdAndPeriodStartAndPeriodEnd(
            Long subscriptionId,
            LocalDateTime periodStart,
            LocalDateTime periodEnd
    );
}
