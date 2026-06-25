package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.cars.model.vehicleDetailsDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleDetailsMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.VehicleDetailsJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleDetailsRepository {
    private final VehicleDetailsJpa vehicleDetailsJpa;

    public vehicleDetailsDomain save(vehicleDetailsDomain vehicleDetailsDomain) {
        return VehicleDetailsMapper.toDomain(
                vehicleDetailsJpa.save(
                        VehicleDetailsMapper.toEntity(vehicleDetailsDomain)
                )
        );
    }

    public Optional<vehicleDetailsDomain> findByVehicleId(Long vehicleId) {
        return vehicleDetailsJpa.findByVehicleId(vehicleId)
                .map(VehicleDetailsMapper::toDomain);
    }

    public Optional<vehicleDetailsDomain> findByVehicleIdForUpdate(Long vehicleId) {
        return vehicleDetailsJpa.findByVehicleIdForUpdate(vehicleId)
                .map(VehicleDetailsMapper::toDomain);
    }

    public void updateCurrentKm(Long vehicleId, Integer currentKm) {
        vehicleDetailsJpa.updateCurrentKmByVehicleId(vehicleId, currentKm);
    }
}
