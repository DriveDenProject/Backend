package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.vehicleNotifications.model.MaintenanceCategoryDomain;
import com.driveden.app.infrastructure.out.persistence.entity.MaintenanceCategoryEntity;

public class MaintenanceCategoryMapper {

    public static MaintenanceCategoryDomain toDomain(MaintenanceCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MaintenanceCategoryDomain(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }
}
