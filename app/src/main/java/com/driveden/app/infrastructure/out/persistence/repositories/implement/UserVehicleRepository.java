package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.users.model.UserVehicleDomain;
import com.driveden.app.infrastructure.out.persistence.entity.UserVehicleEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.UserVehicleMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UserVehicleRepositoryJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserVehicleRepository {

    private final UserVehicleRepositoryJpa userVehicleRepositoryJpa;

    public UserVehicleDomain save(UserVehicleEntity entity) {
        UserVehicleEntity savedEntity = userVehicleRepositoryJpa.save(entity);
        return UserVehicleMapper.toDomain(savedEntity);
    }

    public UserVehicleDomain findByIdUserIdAndIsPrimaryTrue(Long userId) {
        return userVehicleRepositoryJpa.findByIdUserIdAndIsPrimaryTrue(userId)
                .map(UserVehicleMapper::toDomain)
                .orElse(null);
    }

}
