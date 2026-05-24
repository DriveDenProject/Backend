package com.driveden.app.domain.fuelLogs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuelLogResponseDTO(
        Long id,
        Long vehicleId,
        BigDecimal gallons,
        BigDecimal priceTotal,
        BigDecimal pricePerGallon,
        Integer kmAtFill,
        LocalDateTime filledAt,
        String gasStation
) {
}
