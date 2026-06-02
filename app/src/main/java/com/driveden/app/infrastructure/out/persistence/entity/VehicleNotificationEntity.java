package com.driveden.app.infrastructure.out.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationPriority;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "vehicle_notifications")
public class VehicleNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "service_name", nullable = false, length = 150)
    private String serviceName;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "reminder_frequency_days", nullable = false)
    private Integer reminderFrequencyDays;

    @Column(name = "notify_before_days", nullable = false)
    private Integer notifyBeforeDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private VehicleNotificationPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private VehicleNotificationStatus status;

    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring;

    @Column(name = "recurrence_interval_days")
    private Integer recurrenceIntervalDays;

    @Column(name = "last_notification_sent")
    private LocalDateTime lastNotificationSent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
