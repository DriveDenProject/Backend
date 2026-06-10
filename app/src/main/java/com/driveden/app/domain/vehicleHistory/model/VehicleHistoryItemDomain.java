package com.driveden.app.domain.vehicleHistory.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VehicleHistoryItemDomain(
        String type,
        Long id,
        String title,
        BigDecimal amount,
        LocalDate date
) {
}
