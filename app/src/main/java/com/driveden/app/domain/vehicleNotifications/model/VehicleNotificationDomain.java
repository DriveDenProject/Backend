package com.driveden.app.domain.vehicleNotifications.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleNotificationDomain {

    private Long id;
    private Long vehicleId;
    private Long categoryId;
    private String serviceName;
    private String description;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Integer reminderFrequencyDays;
    private Integer notifyBeforeDays;
    private VehicleNotificationPriority priority;
    private VehicleNotificationStatus status;
    private Boolean isRecurring;
    private Integer recurrenceIntervalDays;
    private LocalDateTime lastNotificationSent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
