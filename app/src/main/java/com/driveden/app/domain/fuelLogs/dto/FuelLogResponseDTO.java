package com.driveden.app.domain.fuelLogs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FuelLogResponseDTO(
        Long id,
        Long vehicleId,
        BigDecimal gallons,
        BigDecimal priceTotal,
        BigDecimal pricePerGallon,
        Integer kmAtFill,
        LocalDateTime filledAt,
        String gasStation,
        @JsonProperty("payment_method_id")
        Long paymentMethodId,
        String notes
) {
}
