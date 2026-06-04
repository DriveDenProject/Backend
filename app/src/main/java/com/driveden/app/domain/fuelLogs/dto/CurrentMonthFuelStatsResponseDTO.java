package com.driveden.app.domain.fuelLogs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CurrentMonthFuelStatsResponseDTO(
        BigDecimal monthlyExpense,
        LocalDateTime lastFuelingDate,
        BigDecimal fuelEfficiency
) {
}
