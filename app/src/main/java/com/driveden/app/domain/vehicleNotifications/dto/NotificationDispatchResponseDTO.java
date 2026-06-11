package com.driveden.app.domain.vehicleNotifications.dto;

public record NotificationDispatchResponseDTO(
        Boolean sent,
        VehicleNotificationResponseDTO notification
) {
}
