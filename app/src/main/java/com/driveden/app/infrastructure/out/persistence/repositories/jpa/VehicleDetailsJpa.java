package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.VehicleDetailsEntity;

public interface VehicleDetailsJpa extends JpaRepository<VehicleDetailsEntity, Long> {

    Optional<VehicleDetailsEntity> findByVehicleId(Long vehicleId);

    @Modifying
    @Query("UPDATE VehicleDetailsEntity vd SET vd.currentKm = :currentKm WHERE vd.vehicleId = :vehicleId")
    int updateCurrentKmByVehicleId(@Param("vehicleId") Long vehicleId, @Param("currentKm") Integer currentKm);

}
