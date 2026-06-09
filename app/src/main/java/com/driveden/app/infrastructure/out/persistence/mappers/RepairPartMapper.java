package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.repairs.model.RepairPartDomain;
import com.driveden.app.infrastructure.out.persistence.entity.RepairPartEntity;
import com.driveden.app.infrastructure.out.persistence.entity.ids.RepairPartId;

public class RepairPartMapper {

    public static RepairPartDomain toDomain(RepairPartEntity entity) {
        if (entity == null) {
            return null;
        }

        return new RepairPartDomain(
                entity.getId().getRepairId(),
                entity.getId().getPartId(),
                entity.getQuantity(),
                entity.getCost(),
                entity.getWarrantyExpiration(),
                entity.getPartExpiration()
        );
    }

    public static RepairPartEntity toEntity(RepairPartDomain domain) {
        if (domain == null) {
            return null;
        }

        RepairPartEntity entity = new RepairPartEntity();
        entity.setId(new RepairPartId(domain.getRepairId(), domain.getPartId()));
        entity.setQuantity(domain.getQuantity());
        entity.setCost(domain.getCost());
        entity.setWarrantyExpiration(domain.getWarrantyExpiration());
        entity.setPartExpiration(domain.getPartExpiration());
        return entity;
    }
}
