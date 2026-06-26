package com.driveden.app.domain.subscriptions.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    private Long id;
    private Long userId;
    private SubscriptionPlan plan;
    private SubscriptionProvider provider;
    private String providerSubscriptionId;
    private SubscriptionStatus status;
    private LocalDateTime startsAt;
    private LocalDateTime expiresAt;
    private Boolean autoRenew;
    private Boolean trial;
    private LocalDateTime cancelledAt;
    private LocalDateTime gracePeriodUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isActiveAt(LocalDateTime now) {
        boolean started = startsAt == null || !startsAt.isAfter(now);
        boolean notExpired = expiresAt == null || expiresAt.isAfter(now);
        boolean withinGrace = cancelledAt == null
                || (gracePeriodUntil != null && !gracePeriodUntil.isBefore(now));

        return status == SubscriptionStatus.ACTIVE && started && notExpired && withinGrace;
    }
}
