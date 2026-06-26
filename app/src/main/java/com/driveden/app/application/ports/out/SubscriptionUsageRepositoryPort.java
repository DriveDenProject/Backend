package com.driveden.app.application.ports.out;

import java.time.LocalDateTime;
import java.util.Optional;

import com.driveden.app.domain.subscriptions.model.SubscriptionUsage;

public interface SubscriptionUsageRepositoryPort {

    Optional<SubscriptionUsage> findBySubscriptionIdAndPeriod(
            Long subscriptionId,
            LocalDateTime periodStart,
            LocalDateTime periodEnd
    );

    SubscriptionUsage save(SubscriptionUsage usage);
}
