package com.driveden.app.domain.repairs.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RepairHistoryDomain(
        Long repairId,
        String name,
        BigDecimal cost,
        LocalDate repairDate
) {
}
