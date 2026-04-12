package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.UserVehicleEntity;
import com.driveden.app.infrastructure.out.persistence.entity.ids.UserVehicleId;

public interface UserVehicleRepositoryJpa extends JpaRepository<UserVehicleEntity, UserVehicleId> {

    List<UserVehicleEntity> findByIdUserId(Long userId);

    Optional<UserVehicleEntity> findByIdUserIdAndIsPrimaryTrue(Long userId);

    boolean existsByIdUserIdAndIdVehicleId(Long userId, Long vehicleId);

    void deleteByIdUserIdAndIdVehicleId(Long userId, Long vehicleId);

    @Modifying
    @Query("UPDATE UserVehicleEntity uv SET uv.isPrimary = false WHERE uv.id.userId = :userId")
    void clearPrimaryVehicle(@Param("userId") Long userId);

}
