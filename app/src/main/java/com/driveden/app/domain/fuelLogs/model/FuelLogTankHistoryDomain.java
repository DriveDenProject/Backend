package com.driveden.app.domain.fuelLogs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuelLogTankHistoryDomain(
        Long id,
        LocalDateTime filledAt,
        String notes,
        String gasStation,
        BigDecimal priceTotal
) {
}
