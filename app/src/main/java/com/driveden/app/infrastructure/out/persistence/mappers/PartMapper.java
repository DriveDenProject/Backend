package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.repairs.model.PartDomain;
import com.driveden.app.infrastructure.out.persistence.entity.PartEntity;

public class PartMapper {

    public static PartDomain toDomain(PartEntity entity) {
        if (entity == null) {
            return null;
        }

        return new PartDomain(
                entity.getId(),
                entity.getCategoryId(),
                entity.getName(),
                entity.getBrand()
        );
    }

    public static PartEntity toEntity(PartDomain domain) {
        if (domain == null) {
            return null;
        }

        PartEntity entity = new PartEntity();
        entity.setId(domain.getId());
        entity.setCategoryId(domain.getCategoryId());
        entity.setName(domain.getName());
        entity.setBrand(domain.getBrand());
        return entity;
    }
}
