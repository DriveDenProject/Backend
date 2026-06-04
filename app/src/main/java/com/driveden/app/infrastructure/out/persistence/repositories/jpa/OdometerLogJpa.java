package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.domain.odometerLogs.model.OdometerLogSource;
import com.driveden.app.infrastructure.out.persistence.entity.OdometerLogEntity;
import com.driveden.app.infrastructure.out.persistence.projection.MonthlyMileageStatsProjection;

public interface OdometerLogJpa extends JpaRepository<OdometerLogEntity, Long> {

    Optional<OdometerLogEntity> findBySourceAndSourceId(OdometerLogSource source, Long sourceId);

    Optional<OdometerLogEntity> findFirstByVehicleIdOrderByRecordedAtDescIdDesc(Long vehicleId);

    @Query(value = """
        SELECT
            TO_CHAR(recorded_at, 'YYYY-MM') AS month,
            CASE
                WHEN COUNT(*) < 2 THEN 0
                ELSE MAX(km) - MIN(km)
            END AS "kmTraveled"
        FROM odometer_logs
        WHERE vehicle_id = :vehicleId
        GROUP BY DATE_TRUNC('month', recorded_at), TO_CHAR(recorded_at, 'YYYY-MM')
        ORDER BY DATE_TRUNC('month', recorded_at) ASC
    """, nativeQuery = true)
    List<MonthlyMileageStatsProjection> findMonthlyMileageStats(@Param("vehicleId") Long vehicleId);

    @Query(value = """
        SELECT
            CASE
                WHEN COUNT(*) < 2 THEN 0
                ELSE MAX(km) - MIN(km)
            END AS "kmTraveled"
        FROM odometer_logs
        WHERE vehicle_id = :vehicleId
        AND recorded_at >= :startDate
        AND recorded_at < :endDate
    """, nativeQuery = true)
    Number calculateKmTraveledByVehicleIdAndRecordedAtBetween(
            @Param("vehicleId") Long vehicleId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT OL
        FROM OdometerLogEntity OL
        WHERE OL.vehicleId = :vehicleId
        AND OL.id <> :currentLogId
        AND (
            OL.recordedAt < :recordedAt
            OR (OL.recordedAt = :recordedAt AND OL.id < :currentLogId)
        )
        ORDER BY OL.recordedAt DESC, OL.id DESC
    """)
    List<OdometerLogEntity> findPreviousLogs(
            @Param("vehicleId") Long vehicleId,
            @Param("recordedAt") LocalDateTime recordedAt,
            @Param("currentLogId") Long currentLogId
    );

    @Query("""
        SELECT OL
        FROM OdometerLogEntity OL
        WHERE OL.vehicleId = :vehicleId
        AND OL.id <> :currentLogId
        AND (
            OL.recordedAt > :recordedAt
            OR (OL.recordedAt = :recordedAt AND OL.id > :currentLogId)
        )
        ORDER BY OL.recordedAt ASC, OL.id ASC
    """)
    List<OdometerLogEntity> findNextLogs(
            @Param("vehicleId") Long vehicleId,
            @Param("recordedAt") LocalDateTime recordedAt,
            @Param("currentLogId") Long currentLogId
    );
}
