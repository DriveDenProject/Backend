package com.driveden.app.domain.fuelLogs.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LastFuelLogResponseDTO(
        LocalDate date,
        BigDecimal cost,
        BigDecimal gallons,
        Integer km
) {
}
