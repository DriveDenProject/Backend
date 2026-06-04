package com.driveden.app.domain.fuelLogs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FuelLogEfficiencyDomain(
        Integer kmAtFill,
        BigDecimal gallons,
        LocalDateTime filledAt
) {
}
