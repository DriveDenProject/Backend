package com.driveden.app.domain.odometerLogs.dto;

import java.math.BigDecimal;

public record CurrentMonthMileageStatsResponseDTO(
        Integer kilometersThisMonth,
        BigDecimal averageKilometersPerDay
) {
}
