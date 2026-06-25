package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.VehicleDetailsEntity;

import jakarta.persistence.LockModeType;

public interface VehicleDetailsJpa extends JpaRepository<VehicleDetailsEntity, Long> {

    Optional<VehicleDetailsEntity> findByVehicleId(Long vehicleId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT vd FROM VehicleDetailsEntity vd WHERE vd.vehicleId = :vehicleId")
    Optional<VehicleDetailsEntity> findByVehicleIdForUpdate(@Param("vehicleId") Long vehicleId);

    @Modifying
    @Query("UPDATE VehicleDetailsEntity vd SET vd.currentKm = :currentKm WHERE vd.vehicleId = :vehicleId")
    int updateCurrentKmByVehicleId(@Param("vehicleId") Long vehicleId, @Param("currentKm") Integer currentKm);

}
