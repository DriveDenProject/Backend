package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.VehicleHistoryRepositoryPort;
import com.driveden.app.domain.vehicleHistory.model.VehicleHistoryItemDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleHistoryMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.VehicleHistoryJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleHistoryRepository implements VehicleHistoryRepositoryPort {

    private final VehicleHistoryJpa vehicleHistoryJpa;

    @Override
    public Page<VehicleHistoryItemDomain> findByVehicleId(Long vehicleId, Pageable pageable) {
        return vehicleHistoryJpa.findByVehicleId(vehicleId, pageable)
                .map(VehicleHistoryMapper::toDomain);
    }
}
