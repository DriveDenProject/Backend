package com.driveden.app.domain.repairs.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RepairStatsResponseDTO(
        LocalDate lastRepair,
        BigDecimal totalSpent,
        Long totalRepairs
) {
}
