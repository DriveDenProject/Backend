package com.driveden.app.domain.cars.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationPriority;

public record VehicleDashboardResponseDTO(
        BigDecimal monthlyExpenses,
        Integer currentKm,
        NextServiceResponseDTO nextService,
        LastDashboardFuelLogResponseDTO lastFuelLog
) {

    public record NextServiceResponseDTO(
            String serviceName,
            VehicleNotificationPriority priority
    ) {
    }

    public record LastDashboardFuelLogResponseDTO(
            BigDecimal priceTotal,
            LocalDateTime filledAt
    ) {
    }
}
