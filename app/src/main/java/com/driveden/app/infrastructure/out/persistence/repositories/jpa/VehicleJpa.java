package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.VehicleEntity;

public interface VehicleJpa extends JpaRepository<VehicleEntity, Long> {

}
