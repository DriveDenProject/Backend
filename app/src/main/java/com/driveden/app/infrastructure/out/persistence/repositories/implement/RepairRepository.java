package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.RepairRepositoryPort;
import com.driveden.app.domain.repairs.model.LatestRepairByCategoryDomain;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.domain.repairs.model.RepairHistoryDomain;
import com.driveden.app.domain.repairs.model.RepairStatsDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.RepairMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.RepairJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RepairRepository implements RepairRepositoryPort {

    private final RepairJpa repairJpa;

    @Override
    public RepairDomain save(RepairDomain repair) {
        return RepairMapper.toDomain(
                repairJpa.save(
                        RepairMapper.toEntity(repair)
                )
        );
    }

    @Override
    public Page<RepairHistoryDomain> findHistoryByVehicleId(Long vehicleId, Pageable pageable) {
        return repairJpa.findHistoryByVehicleId(vehicleId, pageable)
                .map(RepairMapper::toHistoryDomain);
    }

    @Override
    public RepairStatsDomain findStatsByVehicleId(Long vehicleId) {
        return RepairMapper.toStatsDomain(
                repairJpa.findStatsByVehicleId(vehicleId)
        );
    }

    @Override
    public List<LatestRepairByCategoryDomain> findLatestByVehicleIdAndCategoryId(Long vehicleId, Long categoryId) {
        return repairJpa.findLatestByVehicleIdAndCategoryId(vehicleId, categoryId).stream()
                .map(RepairMapper::toLatestRepairByCategoryDomain)
                .toList();
    }

    @Override
    public BigDecimal sumTotalCostByVehicleIdAndRepairDateBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return repairJpa.sumTotalCostByVehicleIdAndRepairDateBetween(vehicleId, startDate, endDate);
    }
}
