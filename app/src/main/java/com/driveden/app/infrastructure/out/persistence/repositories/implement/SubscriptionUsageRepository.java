package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.SubscriptionUsageRepositoryPort;
import com.driveden.app.domain.subscriptions.model.SubscriptionUsage;
import com.driveden.app.infrastructure.out.persistence.entity.UserSubscriptionEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.SubscriptionUsageMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.SubscriptionUsageJpa;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UserSubscriptionJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubscriptionUsageRepository implements SubscriptionUsageRepositoryPort {

    private final SubscriptionUsageJpa subscriptionUsageJpa;
    private final UserSubscriptionJpa userSubscriptionJpa;

    @Override
    public Optional<SubscriptionUsage> findBySubscriptionIdAndPeriod(
            Long subscriptionId,
            LocalDateTime periodStart,
            LocalDateTime periodEnd
    ) {
        return subscriptionUsageJpa
                .findBySubscriptionIdAndPeriodStartAndPeriodEnd(subscriptionId, periodStart, periodEnd)
                .map(SubscriptionUsageMapper::toDomain);
    }

    @Override
    public SubscriptionUsage save(SubscriptionUsage usage) {
        LocalDateTime now = LocalDateTime.now();
        UserSubscriptionEntity subscription = userSubscriptionJpa.getReferenceById(usage.getSubscriptionId());

        return SubscriptionUsageMapper.toDomain(subscriptionUsageJpa.save(
                SubscriptionUsageMapper.toEntity(usage, subscription, now)
        ));
    }
}
