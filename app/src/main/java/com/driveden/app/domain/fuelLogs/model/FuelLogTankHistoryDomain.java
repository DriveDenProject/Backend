package com.driveden.app.domain.fuelLogs.model;

import java.math.BigDecimal;

public record FuelLogTankHistoryDomain(
        String notes,
        String gasStation,
        BigDecimal priceTotal
) {
}
