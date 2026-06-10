package com.driveden.app.domain.repairs.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RepairStatsDomain(
        LocalDate lastRepair,
        BigDecimal totalSpent,
        Long totalRepairs
) {
}
