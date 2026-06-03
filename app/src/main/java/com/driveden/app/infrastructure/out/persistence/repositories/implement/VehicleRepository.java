package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.cars.model.vehicleDomain;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.VehicleJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleRepository {

    private final VehicleJpa vehicleJpa;

    public vehicleDomain save(vehicleDomain vehicleDomain) {
        VehicleEntity vehicleEntity = VehicleMapper.toEntity(vehicleDomain);
        VehicleEntity savedEntity = vehicleJpa.save(vehicleEntity);
        return VehicleMapper.toDomain(savedEntity);
    }

    public Optional<vehicleDomain> findById(Long id) {
        return vehicleJpa.findById(id)
                .map(VehicleMapper::toDomain);
    }

}
