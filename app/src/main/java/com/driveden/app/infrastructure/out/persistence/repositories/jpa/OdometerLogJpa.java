package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.domain.odometerLogs.model.OdometerLogSource;
import com.driveden.app.infrastructure.out.persistence.entity.OdometerLogEntity;

public interface OdometerLogJpa extends JpaRepository<OdometerLogEntity, Long> {

    Optional<OdometerLogEntity> findBySourceAndSourceId(OdometerLogSource source, Long sourceId);

    Optional<OdometerLogEntity> findFirstByVehicleIdOrderByRecordedAtDescIdDesc(Long vehicleId);

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
