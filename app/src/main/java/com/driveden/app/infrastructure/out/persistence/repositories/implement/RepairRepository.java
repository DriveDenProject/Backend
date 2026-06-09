package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.RepairRepositoryPort;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.RepairMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.RepairJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RepairRepository implements RepairRepositoryPort {

    private final RepairJpa repairJpa;

    @Override
    public RepairDomain save(RepairDomain repair) {
        return RepairMapper.toDomain(
                repairJpa.save(
                        RepairMapper.toEntity(repair)
                )
        );
    }
}
