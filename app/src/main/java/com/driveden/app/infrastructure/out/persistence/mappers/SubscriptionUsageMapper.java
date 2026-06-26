package com.driveden.app.infrastructure.out.persistence.mappers;

import java.time.LocalDateTime;

import com.driveden.app.domain.subscriptions.model.SubscriptionUsage;
import com.driveden.app.infrastructure.out.persistence.entity.SubscriptionUsageEntity;
import com.driveden.app.infrastructure.out.persistence.entity.UserSubscriptionEntity;

public class SubscriptionUsageMapper {

    private SubscriptionUsageMapper() {
    }

    public static SubscriptionUsage toDomain(SubscriptionUsageEntity entity) {
        if (entity == null) {
            return null;
        }

        return SubscriptionUsage.builder()
                .id(entity.getId())
                .subscriptionId(entity.getSubscription().getId())
                .periodStart(entity.getPeriodStart())
                .periodEnd(entity.getPeriodEnd())
                .scansUsed(entity.getScansUsed())
                .audiosUsed(entity.getAudiosUsed())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SubscriptionUsageEntity toEntity(
            SubscriptionUsage domain,
            UserSubscriptionEntity subscription,
            LocalDateTime now
    ) {
        SubscriptionUsageEntity entity = new SubscriptionUsageEntity();
        entity.setId(domain.getId());
        entity.setSubscription(subscription);
        entity.setPeriodStart(domain.getPeriodStart());
        entity.setPeriodEnd(domain.getPeriodEnd());
        entity.setScansUsed(domain.getScansUsed() == null ? 0 : domain.getScansUsed());
        entity.setAudiosUsed(domain.getAudiosUsed() == null ? 0 : domain.getAudiosUsed());
        entity.setCreatedAt(domain.getCreatedAt() == null ? now : domain.getCreatedAt());
        entity.setUpdatedAt(now);
        return entity;
    }
}
