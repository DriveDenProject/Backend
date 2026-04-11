package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.transmissionType.model.transmissionTypeDomain;
import com.driveden.app.infrastructure.out.persistence.entity.TransmissionTypesEntity;

public class TransmissionTypeMapper {
    
    public static transmissionTypeDomain toDomain(TransmissionTypesEntity entity) {
        if (entity == null) {
            return null;
        }
        return new transmissionTypeDomain(
                entity.getId(),
                entity.getName()
        );
    }

    public static TransmissionTypesEntity toEntity(transmissionTypeDomain domain) {
        if (domain == null) {
            return null;
        }
        TransmissionTypesEntity entity = new TransmissionTypesEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }
}
