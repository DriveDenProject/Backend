package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.repairs.dto.LatestRepairByCategoryResponseDTO;
import com.driveden.app.domain.repairs.dto.RegisterRepairDTO;
import com.driveden.app.domain.repairs.dto.RepairHistoryResponseDTO;
import com.driveden.app.domain.repairs.dto.RepairStatsResponseDTO;
import com.driveden.app.domain.repairs.model.LatestRepairByCategoryDomain;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.domain.repairs.model.RepairHistoryDomain;
import com.driveden.app.domain.repairs.model.RepairStatsDomain;
import com.driveden.app.infrastructure.out.persistence.entity.RepairEntity;
import com.driveden.app.infrastructure.out.persistence.projection.LatestRepairByCategoryProjection;
import com.driveden.app.infrastructure.out.persistence.projection.RepairHistoryProjection;
import com.driveden.app.infrastructure.out.persistence.projection.RepairStatsProjection;

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

    public static RepairHistoryDomain toHistoryDomain(RepairHistoryProjection projection) {
        if (projection == null) {
            return null;
        }

        return new RepairHistoryDomain(
                projection.getRepairId(),
                projection.getName(),
                projection.getCost(),
                projection.getRepairDate().toLocalDate()
        );
    }

    public static RepairStatsDomain toStatsDomain(RepairStatsProjection projection) {
        if (projection == null) {
            return new RepairStatsDomain(null, java.math.BigDecimal.ZERO, 0L);
        }

        return new RepairStatsDomain(
                projection.getLastRepair() == null ? null : projection.getLastRepair().toLocalDate(),
                projection.getTotalSpent(),
                projection.getTotalRepairs()
        );
    }

    public static LatestRepairByCategoryDomain toLatestRepairByCategoryDomain(
            LatestRepairByCategoryProjection projection
    ) {
        if (projection == null) {
            return null;
        }

        return new LatestRepairByCategoryDomain(
                projection.getDescription(),
                projection.getRepairDate().toLocalDate()
        );
    }

    public static RepairHistoryResponseDTO toHistoryResponseDTO(RepairHistoryDomain domain) {
        if (domain == null) {
            return null;
        }

        return new RepairHistoryResponseDTO(
                domain.repairId(),
                domain.name(),
                domain.cost(),
                domain.repairDate()
        );
    }

    public static LatestRepairByCategoryResponseDTO toLatestRepairByCategoryResponseDTO(
            LatestRepairByCategoryDomain domain
    ) {
        if (domain == null) {
            return null;
        }

        return new LatestRepairByCategoryResponseDTO(
                domain.description(),
                domain.repairDate()
        );
    }

    public static RepairStatsResponseDTO toStatsResponseDTO(RepairStatsDomain domain) {
        if (domain == null) {
            return null;
        }

        return new RepairStatsResponseDTO(
                domain.lastRepair(),
                domain.totalSpent(),
                domain.totalRepairs()
        );
    }
}
