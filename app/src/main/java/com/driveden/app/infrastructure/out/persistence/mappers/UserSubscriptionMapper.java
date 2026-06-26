package com.driveden.app.infrastructure.out.persistence.mappers;

import java.time.LocalDateTime;

import com.driveden.app.domain.subscriptions.dto.CurrentSubscriptionResponseDTO;
import com.driveden.app.domain.subscriptions.model.Subscription;
import com.driveden.app.infrastructure.out.persistence.entity.SubscriptionPlanEntity;
import com.driveden.app.infrastructure.out.persistence.entity.UserSubscriptionEntity;
import com.driveden.app.infrastructure.out.persistence.entity.UsersEntity;

public class UserSubscriptionMapper {

    private UserSubscriptionMapper() {
    }

    public static Subscription toDomain(UserSubscriptionEntity entity) {
        if (entity == null) {
            return null;
        }

        return Subscription.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .plan(SubscriptionPlanMapper.toDomain(entity.getPlan()))
                .provider(entity.getProvider())
                .providerSubscriptionId(entity.getProviderSubscriptionId())
                .status(entity.getStatus())
                .startsAt(entity.getStartsAt())
                .expiresAt(entity.getExpiresAt())
                .autoRenew(entity.getAutoRenew())
                .trial(entity.getTrial())
                .cancelledAt(entity.getCancelledAt())
                .gracePeriodUntil(entity.getGracePeriodUntil())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UserSubscriptionEntity toEntity(
            Subscription domain,
            UsersEntity user,
            SubscriptionPlanEntity plan,
            LocalDateTime now
    ) {
        UserSubscriptionEntity entity = new UserSubscriptionEntity();
        entity.setId(domain.getId());
        entity.setUser(user);
        entity.setPlan(plan);
        entity.setProvider(domain.getProvider());
        entity.setProviderSubscriptionId(domain.getProviderSubscriptionId());
        entity.setStatus(domain.getStatus());
        entity.setStartsAt(domain.getStartsAt());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setAutoRenew(Boolean.TRUE.equals(domain.getAutoRenew()));
        entity.setTrial(Boolean.TRUE.equals(domain.getTrial()));
        entity.setCancelledAt(domain.getCancelledAt());
        entity.setGracePeriodUntil(domain.getGracePeriodUntil());
        entity.setCreatedAt(domain.getCreatedAt() == null ? now : domain.getCreatedAt());
        entity.setUpdatedAt(now);
        return entity;
    }

    public static CurrentSubscriptionResponseDTO toCurrentResponseDTO(Subscription subscription) {
        return new CurrentSubscriptionResponseDTO(
                subscription.getPlan().getCode(),
                subscription.getPlan().getName(),
                subscription.getStatus().name(),
                subscription.getExpiresAt()
        );
    }
}
