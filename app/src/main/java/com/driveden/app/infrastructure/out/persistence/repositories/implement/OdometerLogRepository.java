package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.OdometerLogRepositoryPort;
import com.driveden.app.domain.odometerLogs.model.OdometerLogDomain;
import com.driveden.app.domain.odometerLogs.model.OdometerLogSource;
import com.driveden.app.infrastructure.out.persistence.mappers.OdometerLogMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.OdometerLogJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OdometerLogRepository implements OdometerLogRepositoryPort {

    private final OdometerLogJpa odometerLogJpa;

    @Override
    public OdometerLogDomain save(OdometerLogDomain odometerLogDomain) {
        return OdometerLogMapper.toDomain(
                odometerLogJpa.save(
                        OdometerLogMapper.toEntity(odometerLogDomain)
                )
        );
    }

    @Override
    public Optional<OdometerLogDomain> findBySourceAndSourceId(OdometerLogSource source, Long sourceId) {
        return odometerLogJpa.findBySourceAndSourceId(source, sourceId)
                .map(OdometerLogMapper::toDomain);
    }

    @Override
    public Optional<OdometerLogDomain> findPreviousLog(
            Long vehicleId,
            LocalDateTime recordedAt,
            Long currentLogId
    ) {
        return odometerLogJpa.findPreviousLogs(vehicleId, recordedAt, currentLogId).stream()
                .findFirst()
                .map(OdometerLogMapper::toDomain);
    }

    @Override
    public Optional<OdometerLogDomain> findNextLog(
            Long vehicleId,
            LocalDateTime recordedAt,
            Long currentLogId
    ) {
        return odometerLogJpa.findNextLogs(vehicleId, recordedAt, currentLogId).stream()
                .findFirst()
                .map(OdometerLogMapper::toDomain);
    }

    @Override
    public Optional<OdometerLogDomain> findLatestByVehicleId(Long vehicleId) {
        return odometerLogJpa.findFirstByVehicleIdOrderByRecordedAtDescIdDesc(vehicleId)
                .map(OdometerLogMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        odometerLogJpa.deleteById(id);
    }
}
