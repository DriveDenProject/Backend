package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.UserVehicleEntity;
import com.driveden.app.infrastructure.out.persistence.entity.ids.UserVehicleId;
import com.driveden.app.infrastructure.out.persistence.projection.UserDetailsProjection;

public interface UserVehicleRepositoryJpa extends JpaRepository<UserVehicleEntity, UserVehicleId> {

    List<UserVehicleEntity> findByIdUserId(Long userId);

    List<UserVehicleEntity> findByIdVehicleId(Long vehicleId);

    Optional<UserVehicleEntity> findByIdUserIdAndIsPrimaryTrue(Long userId);

    boolean existsByIdUserIdAndIdVehicleId(Long userId, Long vehicleId);

    long countByIdUserId(Long userId);

    void deleteByIdUserIdAndIdVehicleId(Long userId, Long vehicleId);

    @Modifying
    @Query("UPDATE UserVehicleEntity uv SET uv.isPrimary = false WHERE uv.id.userId = :userId")
    void clearPrimaryVehicle(@Param("userId") Long userId);

    @Query("""
        SELECT
            V.id AS vehicleId,
            U.username AS username,
            V.nickName AS nickname,
            V.brand AS brand,
            V.model AS model,
            V.year AS year
        FROM UserVehicleEntity UV
        JOIN UV.user U
        JOIN UV.vehicle V
        WHERE UV.isPrimary = true
        AND U.id = :userId
    """)
    Optional<UserDetailsProjection> findPrimaryVehicleDetailsByUserId(@Param("userId") Long userId);

}
