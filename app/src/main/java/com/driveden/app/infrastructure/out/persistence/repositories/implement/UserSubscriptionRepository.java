package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.UserSubscriptionRepositoryPort;
import com.driveden.app.domain.subscriptions.model.Subscription;
import com.driveden.app.domain.subscriptions.model.SubscriptionStatus;
import com.driveden.app.infrastructure.out.persistence.entity.SubscriptionPlanEntity;
import com.driveden.app.infrastructure.out.persistence.entity.UsersEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.UserSubscriptionMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.SubscriptionPlanJpa;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UserSubscriptionJpa;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UsersJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserSubscriptionRepository implements UserSubscriptionRepositoryPort {

    private final UserSubscriptionJpa userSubscriptionJpa;
    private final UsersJpa usersJpa;
    private final SubscriptionPlanJpa subscriptionPlanJpa;

    @Override
    public Optional<Subscription> findActiveByUserId(Long userId, LocalDateTime now) {
        return userSubscriptionJpa.findActiveByUserId(userId, SubscriptionStatus.ACTIVE, now).stream()
                .findFirst()
                .map(UserSubscriptionMapper::toDomain);
    }

    @Override
    public Subscription save(Subscription subscription) {
        LocalDateTime now = LocalDateTime.now();
        UsersEntity user = usersJpa.getReferenceById(subscription.getUserId());
        SubscriptionPlanEntity plan = subscriptionPlanJpa.getReferenceById(subscription.getPlan().getId());

        return UserSubscriptionMapper.toDomain(userSubscriptionJpa.save(
                UserSubscriptionMapper.toEntity(subscription, user, plan, now)
        ));
    }
}
