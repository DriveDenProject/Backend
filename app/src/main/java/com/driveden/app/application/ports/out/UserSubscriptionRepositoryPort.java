package com.driveden.app.application.ports.out;

import java.time.LocalDateTime;
import java.util.Optional;

import com.driveden.app.domain.subscriptions.model.Subscription;

public interface UserSubscriptionRepositoryPort {

    Optional<Subscription> findActiveByUserId(Long userId, LocalDateTime now);

    Subscription save(Subscription subscription);
}
