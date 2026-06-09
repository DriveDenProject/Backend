package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.RepairPartRepositoryPort;
import com.driveden.app.domain.repairs.model.RepairPartDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.RepairPartMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.RepairPartJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RepairPartRepository implements RepairPartRepositoryPort {

    private final RepairPartJpa repairPartJpa;

    @Override
    public List<RepairPartDomain> saveAll(List<RepairPartDomain> repairParts) {
        return repairPartJpa.saveAll(
                        repairParts.stream()
                                .map(RepairPartMapper::toEntity)
                                .toList()
                )
                .stream()
                .map(RepairPartMapper::toDomain)
                .toList();
    }
}
