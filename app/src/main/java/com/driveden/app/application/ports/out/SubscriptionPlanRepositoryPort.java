package com.driveden.app.application.ports.out;

import java.util.List;
import java.util.Optional;

import com.driveden.app.domain.subscriptions.model.SubscriptionPlan;

public interface SubscriptionPlanRepositoryPort {

    Optional<SubscriptionPlan> findActiveByCode(String code);

    List<SubscriptionPlan> findAllActive();
}
