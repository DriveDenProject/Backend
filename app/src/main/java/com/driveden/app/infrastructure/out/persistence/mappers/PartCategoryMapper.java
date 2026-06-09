package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.repairs.model.PartCategoryDomain;
import com.driveden.app.infrastructure.out.persistence.entity.PartCategoryEntity;

public class PartCategoryMapper {

    public static PartCategoryDomain toDomain(PartCategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new PartCategoryDomain(
                entity.getId(),
                entity.getName()
        );
    }
}
