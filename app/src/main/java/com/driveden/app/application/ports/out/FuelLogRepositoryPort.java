package com.driveden.app.application.ports.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.driveden.app.domain.fuelLogs.model.FuelLogEfficiencyDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogTankHistoryDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;

public interface FuelLogRepositoryPort {

    FuelLogsDomain save(FuelLogsDomain fuelLogsDomain);

    List<FuelLogsDomain> findByVehicleId(Long vehicleId);

    List<FuelLogsDomain> findByVehicleIdAndFilledAtBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<FuelLogsDomain> findLastFourByVehicleId(Long vehicleId);

    Optional<FuelLogsDomain> findById(Long id);

    boolean existsMoreRecentFuelLog(Long vehicleId, LocalDateTime filledAt);

    Optional<FuelLogsDomain> findLatestByVehicleId(Long vehicleId);

    Page<FuelLogTankHistoryDomain> findTankHistoryByVehicleId(Long vehicleId, Pageable pageable);

    BigDecimal sumPriceTotalByVehicleIdAndFilledAtBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<FuelLogEfficiencyDomain> findLatestTwoByVehicleId(Long vehicleId);

    void deleteById(Long id);
}
