package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.FuelLogsEntity;

public interface FuelLogsJpa extends JpaRepository<FuelLogsEntity, Long> {

    List<FuelLogsEntity> findByVehicleIdOrderByFilledAtDesc(Long vehicleId);
}
