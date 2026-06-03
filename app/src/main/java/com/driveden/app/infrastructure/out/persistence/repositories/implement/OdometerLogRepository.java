package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.OdometerLogRepositoryPort;
import com.driveden.app.domain.odometerLogs.model.OdometerLogDomain;
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
}
