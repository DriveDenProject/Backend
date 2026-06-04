package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.FuelLogsEntity;

public interface FuelLogsJpa extends JpaRepository<FuelLogsEntity, Long> {

    List<FuelLogsEntity> findByVehicleIdOrderByFilledAtDesc(Long vehicleId);

    List<FuelLogsEntity> findByVehicleIdAndFilledAtGreaterThanEqualAndFilledAtLessThanEqualOrderByFilledAtAsc(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<FuelLogsEntity> findTop4ByVehicleIdOrderByFilledAtDescIdDesc(Long vehicleId);

    boolean existsByVehicleIdAndFilledAtAfter(Long vehicleId, LocalDateTime filledAt);

    Optional<FuelLogsEntity> findFirstByVehicleIdOrderByFilledAtDescIdDesc(Long vehicleId);
}
