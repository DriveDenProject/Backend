package com.driveden.app.domain.vehicleNotifications.dto;

import java.time.LocalDate;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVehicleNotificationDTO {

    @NotNull(message = "vehicleId is required")
    @Positive(message = "vehicleId must be positive")
    private Long vehicleId;

    @NotNull(message = "categoryId is required")
    @Positive(message = "categoryId must be positive")
    private Long categoryId;

    @NotBlank(message = "serviceName is required")
    @Size(max = 150, message = "serviceName must be at most 150 characters")
    private String serviceName;

    private String description;

    @NotNull(message = "startDate is required")
    private LocalDate startDate;

    @NotNull(message = "dueDate is required")
    private LocalDate dueDate;

    @NotNull(message = "reminderFrequencyDays is required")
    @Positive(message = "reminderFrequencyDays must be greater than 0")
    private Integer reminderFrequencyDays;

    @NotNull(message = "notifyBeforeDays is required")
    @PositiveOrZero(message = "notifyBeforeDays must be zero or positive")
    private Integer notifyBeforeDays;

    @NotNull(message = "priority is required")
    private VehicleNotificationPriority priority;

    @NotNull(message = "isRecurring is required")
    private Boolean isRecurring;

    private Integer recurrenceIntervalDays;
}
