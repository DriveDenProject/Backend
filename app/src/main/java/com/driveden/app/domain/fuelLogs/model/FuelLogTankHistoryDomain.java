package com.driveden.app.domain.fuelLogs.model;

import java.math.BigDecimal;

public record FuelLogTankHistoryDomain(
        Long id,
        String notes,
        String gasStation,
        BigDecimal priceTotal
) {
}
