package com.driveden.app.application.ports.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.driveden.app.domain.odometerLogs.model.OdometerLogDomain;
import com.driveden.app.domain.odometerLogs.model.OdometerLogSource;
import com.driveden.app.domain.odometerLogs.model.MonthlyMileageStatsDomain;

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

    List<MonthlyMileageStatsDomain> findMonthlyMileageStats(Long vehicleId);

    Integer calculateKmTraveledByVehicleIdAndRecordedAtBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    void deleteById(Long id);
}
