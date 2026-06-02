package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.vehicleNotifications.model.MaintenanceCategoryDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.MaintenanceCategoryMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.MaintenanceCategoryJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MaintenanceCategoryRepository {

    private final MaintenanceCategoryJpa maintenanceCategoryJpa;

    public Optional<MaintenanceCategoryDomain> findById(Long id) {
        return maintenanceCategoryJpa.findById(id)
                .map(MaintenanceCategoryMapper::toDomain);
    }

    public boolean existsById(Long id) {
        return maintenanceCategoryJpa.existsById(id);
    }
}
