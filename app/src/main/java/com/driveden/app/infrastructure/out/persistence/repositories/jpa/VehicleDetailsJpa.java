package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.VehicleDetailsEntity;

public interface VehicleDetailsJpa extends JpaRepository<VehicleDetailsEntity, Long> {

}
