package com.driveden.app.infrastructure.out.persistence.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.driveden.app.domain.subscriptions.dto.SubscriptionPlanResponseDTO;
import com.driveden.app.domain.subscriptions.model.SubscriptionPlan;
import com.driveden.app.infrastructure.out.persistence.entity.SubscriptionFeatureEntity;
import com.driveden.app.infrastructure.out.persistence.entity.SubscriptionPlanEntity;

public class SubscriptionPlanMapper {

    private SubscriptionPlanMapper() {
    }

    public static SubscriptionPlan toDomain(SubscriptionPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        return SubscriptionPlan.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .monthlyPrice(entity.getMonthlyPrice())
                .yearlyPrice(entity.getYearlyPrice())
                .currency(entity.getCurrency())
                .maxVehicles(entity.getMaxVehicles())
                .maxMonthlyScanImgs(entity.getMaxMonthlyScanImgs())
                .maxMonthlyAudios(entity.getMaxMonthlyAudios())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SubscriptionPlan toDomainWithFeatures(SubscriptionPlanEntity entity) {
        SubscriptionPlan domain = toDomain(entity);
        if (domain == null) {
            return null;
        }

        Set<String> featureCodes = entity.getFeatures().stream()
                .map(SubscriptionFeatureEntity::getCode)
                .collect(Collectors.toSet());
        domain.setFeatureCodes(featureCodes);
        return domain;
    }

    public static SubscriptionPlanResponseDTO toResponseDTO(SubscriptionPlan plan) {
        return new SubscriptionPlanResponseDTO(
                plan.getCode(),
                plan.getName(),
                plan.getMonthlyPrice(),
                plan.getYearlyPrice(),
                plan.getCurrency(),
                plan.getMaxVehicles(),
                plan.getMaxMonthlyScanImgs(),
                plan.getMaxMonthlyAudios()
        );
    }
}
