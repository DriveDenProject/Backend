package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.fuelType.model.FuelTypeDomain;
import com.driveden.app.infrastructure.out.persistence.entity.FuelTypesEntity;

public class FuelTypeMapper {
    
    public static FuelTypeDomain toDomain(FuelTypesEntity entity) {
        if (entity == null) {
            return null;
        }
        return new FuelTypeDomain(
                entity.getId(),
                entity.getName()
        );
    }

    public static FuelTypesEntity toEntity(FuelTypeDomain domain) {
        if (domain == null) {
            return null;
        }
        FuelTypesEntity entity = new FuelTypesEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }   
}
