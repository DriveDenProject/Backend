package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.users.model.UserVehicleDomain;
import com.driveden.app.infrastructure.out.persistence.entity.UserVehicleEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.UserVehicleMapper;
import com.driveden.app.infrastructure.out.persistence.projection.UserDetailsProjection;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UsersJpa;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UserVehicleRepositoryJpa;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.VehicleJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserVehicleRepository {

    private final UserVehicleRepositoryJpa userVehicleRepositoryJpa;
    private final UsersJpa usersJpa;
    private final VehicleJpa vehicleJpa;

    public UserVehicleDomain save(UserVehicleDomain userVehicleDomain) {
        UserVehicleEntity entity = UserVehicleMapper.toEntity(
                userVehicleDomain,
                usersJpa.getReferenceById(userVehicleDomain.getUserId()),
                vehicleJpa.getReferenceById(userVehicleDomain.getVehicleId())
        );
        UserVehicleEntity savedEntity = userVehicleRepositoryJpa.save(entity);
        return UserVehicleMapper.toDomain(savedEntity);
    }

    public UserVehicleDomain findByIdUserIdAndIsPrimaryTrue(Long userId) {
        return userVehicleRepositoryJpa.findByIdUserIdAndIsPrimaryTrue(userId)
                .map(UserVehicleMapper::toDomain)
                .orElse(null);
    }

    public UserDetailsProjection findPrimaryVehicleByUserId(Long userId) {
        return userVehicleRepositoryJpa.findPrimaryVehicleByUserId(userId);
    }

}
