package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleNotificationEntity;

public interface VehicleNotificationJpa extends JpaRepository<VehicleNotificationEntity, Long> {

    List<VehicleNotificationEntity> findByVehicleIdOrderByDueDateAsc(Long vehicleId);

    @Query("""
        SELECT VN
        FROM VehicleNotificationEntity VN
        JOIN UserVehicleEntity UV ON UV.id.vehicleId = VN.vehicleId
        WHERE UV.id.userId = :userId
        ORDER BY VN.dueDate ASC
    """)
    List<VehicleNotificationEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT VN
        FROM VehicleNotificationEntity VN
        WHERE VN.status = :status
        AND VN.dueDate < :today
    """)
    List<VehicleNotificationEntity> findByStatusAndDueDateBefore(
            @Param("status") VehicleNotificationStatus status,
            @Param("today") LocalDate today
    );

    @Query(value = """
        SELECT *
        FROM vehicle_notifications
        WHERE status = 'PENDING'
        AND :today >= (due_date - (notify_before_days * INTERVAL '1 day'))
        AND :today <= due_date
        AND (
            last_notification_sent IS NULL
            OR last_notification_sent <= (:now - (reminder_frequency_days * INTERVAL '1 day'))
        )
    """, nativeQuery = true)
    List<VehicleNotificationEntity> findPendingReadyToDispatch(
            @Param("today") LocalDate today,
            @Param("now") LocalDateTime now
    );
}
