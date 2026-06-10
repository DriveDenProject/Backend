package com.driveden.app.domain.repairs.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RepairHistoryResponseDTO(
        Long repairId,
        String name,
        BigDecimal cost,
        LocalDate repairDate
) {
}
