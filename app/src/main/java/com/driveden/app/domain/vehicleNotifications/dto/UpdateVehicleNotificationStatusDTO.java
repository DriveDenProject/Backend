package com.driveden.app.domain.vehicleNotifications.dto;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVehicleNotificationStatusDTO {

    @NotNull(message = "status is required")
    private VehicleNotificationStatus status;
}
