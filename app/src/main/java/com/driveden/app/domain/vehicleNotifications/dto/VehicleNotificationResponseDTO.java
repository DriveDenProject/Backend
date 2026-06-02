package com.driveden.app.domain.vehicleNotifications.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationPriority;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;

public record VehicleNotificationResponseDTO(
        Long id,
        Long vehicleId,
        Long categoryId,
        String serviceName,
        String description,
        LocalDate startDate,
        LocalDate dueDate,
        Integer reminderFrequencyDays,
        Integer notifyBeforeDays,
        VehicleNotificationPriority priority,
        VehicleNotificationStatus status,
        Boolean isRecurring,
        Integer recurrenceIntervalDays,
        LocalDateTime lastNotificationSent,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
