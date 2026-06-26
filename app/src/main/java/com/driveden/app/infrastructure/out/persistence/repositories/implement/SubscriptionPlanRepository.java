package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.SubscriptionPlanRepositoryPort;
import com.driveden.app.domain.subscriptions.model.SubscriptionPlan;
import com.driveden.app.infrastructure.out.persistence.mappers.SubscriptionPlanMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.SubscriptionPlanJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SubscriptionPlanRepository implements SubscriptionPlanRepositoryPort {

    private final SubscriptionPlanJpa subscriptionPlanJpa;

    @Override
    public Optional<SubscriptionPlan> findActiveByCode(String code) {
        return subscriptionPlanJpa.findByCodeAndActiveTrue(code)
                .map(SubscriptionPlanMapper::toDomain);
    }

    @Override
    public List<SubscriptionPlan> findAllActive() {
        return subscriptionPlanJpa.findByActiveTrueOrderByMonthlyPriceAsc().stream()
                .map(SubscriptionPlanMapper::toDomain)
                .toList();
    }
}
