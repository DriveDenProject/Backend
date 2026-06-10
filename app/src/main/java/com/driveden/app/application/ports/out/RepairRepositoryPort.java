package com.driveden.app.application.ports.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.driveden.app.domain.repairs.model.RepairHistoryDomain;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.domain.repairs.model.RepairStatsDomain;

public interface RepairRepositoryPort {

    RepairDomain save(RepairDomain repair);

    List<RepairHistoryDomain> findHistoryByVehicleId(Long vehicleId);

    RepairStatsDomain findStatsByVehicleId(Long vehicleId);

    BigDecimal sumTotalCostByVehicleIdAndRepairDateBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
