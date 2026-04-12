package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.cars.model.vehicleDetailsDomain;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleDetailsEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleDetailsMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.VehicleDetailsJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleDetailsRepository {
    private final VehicleDetailsJpa vehicleDetailsJpa;

    public vehicleDetailsDomain save(VehicleDetailsEntity vehicleDetailsEntity) {
        // Save entity using JPA repository
        VehicleDetailsEntity savedEntity = vehicleDetailsJpa.save(vehicleDetailsEntity);
        // Convert saved entity back to domain and return
        return VehicleDetailsMapper.toDomain(savedEntity);
    }
    
}
