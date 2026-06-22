package com.driveden.app.domain.fuelLogs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuelLogTankHistoryResponseDTO(
        Long id,
        LocalDateTime filledAt,
        String notes,
        String gasStation,
        BigDecimal priceTotal
) {
}
