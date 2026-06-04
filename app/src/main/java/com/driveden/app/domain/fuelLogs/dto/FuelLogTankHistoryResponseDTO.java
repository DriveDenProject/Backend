package com.driveden.app.domain.fuelLogs.dto;

import java.math.BigDecimal;

public record FuelLogTankHistoryResponseDTO(
        String notes,
        String gasStation,
        BigDecimal priceTotal
) {
}
