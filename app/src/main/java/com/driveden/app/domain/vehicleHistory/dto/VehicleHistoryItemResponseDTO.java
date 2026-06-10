package com.driveden.app.domain.vehicleHistory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VehicleHistoryItemResponseDTO(
        String type,
        Long id,
        String title,
        BigDecimal amount,
        LocalDate date
) {
}
