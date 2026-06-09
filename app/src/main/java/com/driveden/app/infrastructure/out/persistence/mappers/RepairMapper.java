package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.repairs.dto.RegisterRepairDTO;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.infrastructure.out.persistence.entity.RepairEntity;

public class RepairMapper {

    public static RepairDomain toDomain(RepairEntity entity) {
        if (entity == null) {
            return null;
        }

        return new RepairDomain(
                entity.getId(),
                entity.getVehicleId(),
                entity.getRepairDate(),
                entity.getDescription(),
                entity.getWorkshop(),
                entity.getLaborCost(),
                entity.getTotalCost()
        );
    }

    public static RepairEntity toEntity(RepairDomain domain) {
        if (domain == null) {
            return null;
        }

        RepairEntity entity = new RepairEntity();
        entity.setId(domain.getId());
        entity.setVehicleId(domain.getVehicleId());
        entity.setRepairDate(domain.getRepairDate());
        entity.setDescription(domain.getDescription());
        entity.setWorkshop(domain.getWorkshop());
        entity.setLaborCost(domain.getLaborCost());
        entity.setTotalCost(domain.getTotalCost());
        return entity;
    }

    public static RepairDomain fromDTOtoDomain(RegisterRepairDTO dto) {
        if (dto == null) {
            return null;
        }

        return new RepairDomain(
                null,
                dto.getVehicleId(),
                dto.getRepairDate().atStartOfDay(),
                dto.getDescription(),
                dto.getWorkshop(),
                dto.getLaborCost(),
                dto.getTotalCost()
        );
    }
}
