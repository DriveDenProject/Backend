package com.driveden.app.application.ports.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.driveden.app.domain.repairs.model.LatestRepairByCategoryDomain;
import com.driveden.app.domain.repairs.model.RepairHistoryDomain;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.domain.repairs.model.RepairStatsDomain;

public interface RepairRepositoryPort {

    RepairDomain save(RepairDomain repair);

    Page<RepairHistoryDomain> findHistoryByVehicleId(Long vehicleId, Pageable pageable);

    RepairStatsDomain findStatsByVehicleId(Long vehicleId);

    List<LatestRepairByCategoryDomain> findLatestByVehicleIdAndCategoryId(Long vehicleId, Long categoryId);

    BigDecimal sumTotalCostByVehicleIdAndRepairDateBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
