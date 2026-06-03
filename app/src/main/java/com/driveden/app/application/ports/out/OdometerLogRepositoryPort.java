package com.driveden.app.application.ports.out;

import java.time.LocalDateTime;
import java.util.Optional;

import com.driveden.app.domain.odometerLogs.model.OdometerLogDomain;
import com.driveden.app.domain.odometerLogs.model.OdometerLogSource;

public interface OdometerLogRepositoryPort {

    OdometerLogDomain save(OdometerLogDomain odometerLogDomain);

    Optional<OdometerLogDomain> findBySourceAndSourceId(OdometerLogSource source, Long sourceId);

    Optional<OdometerLogDomain> findPreviousLog(
            Long vehicleId,
            LocalDateTime recordedAt,
            Long currentLogId
    );

    Optional<OdometerLogDomain> findNextLog(
            Long vehicleId,
            LocalDateTime recordedAt,
            Long currentLogId
    );

    Optional<OdometerLogDomain> findLatestByVehicleId(Long vehicleId);

    void deleteById(Long id);
}
