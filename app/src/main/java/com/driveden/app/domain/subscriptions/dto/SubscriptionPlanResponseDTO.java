package com.driveden.app.domain.subscriptions.dto;

import java.math.BigDecimal;

public record SubscriptionPlanResponseDTO(
        String code,
        String name,
        BigDecimal monthlyPrice,
        BigDecimal yearlyPrice,
        String currency,
        Integer maxVehicles,
        Integer maxMonthlyScanImgs,
        Integer maxMonthlyAudios
) {
}
