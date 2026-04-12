package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.users.model.UserVehicleDomain;
import com.driveden.app.infrastructure.out.persistence.entity.UserVehicleEntity;
import com.driveden.app.infrastructure.out.persistence.entity.UsersEntity;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleEntity;
import com.driveden.app.infrastructure.out.persistence.entity.ids.UserVehicleId;

public class UserVehicleMapper {

    // Entity → Domain
    public static UserVehicleDomain toDomain(UserVehicleEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserVehicleDomain.builder()
                .userId(entity.getId().getUserId())
                .vehicleId(entity.getId().getVehicleId())
                .isPrimary(entity.getIsPrimary())
                .build();
    }

    // Domain → Entity
    public static UserVehicleEntity toEntity(
            UserVehicleDomain domain,
            UsersEntity user,
            VehicleEntity vehicle
    ) {
        if (domain == null) {
            return null;
        }

        UserVehicleEntity entity = new UserVehicleEntity();

        entity.setId(new UserVehicleId(
                domain.getUserId(),
                domain.getVehicleId()
        ));

        entity.setIsPrimary(domain.getIsPrimary());

        entity.setUser(user);
        entity.setVehicle(vehicle);

        return entity;
    }
}
