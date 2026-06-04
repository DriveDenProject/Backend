package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.driveden.app.application.ports.out.FuelLogRepositoryPort;
import com.driveden.app.domain.fuelLogs.model.FuelLogEfficiencyDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogTankHistoryDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.FuelLogsMapper;
import com.driveden.app.infrastructure.out.persistence.projection.FuelLogEfficiencyProjection;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.FuelLogsJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FuelLogsRepository implements FuelLogRepositoryPort {

    private final FuelLogsJpa fuelLogsJpa;

    @Override
    public FuelLogsDomain save(FuelLogsDomain fuelLogsDomain) {
        return FuelLogsMapper.toDomain(
                fuelLogsJpa.save(
                        FuelLogsMapper.toEntity(fuelLogsDomain)
                )
        );
    }

    @Override
    public List<FuelLogsDomain> findByVehicleId(Long vehicleId) {
        return fuelLogsJpa.findByVehicleIdOrderByFilledAtDesc(vehicleId).stream()
                .map(FuelLogsMapper::toDomain)
                .toList();
    }

    @Override
    public List<FuelLogsDomain> findByVehicleIdAndFilledAtBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return fuelLogsJpa
                .findByVehicleIdAndFilledAtGreaterThanEqualAndFilledAtLessThanEqualOrderByFilledAtAsc(
                        vehicleId,
                        startDate,
                        endDate
                )
                .stream()
                .map(FuelLogsMapper::toDomain)
                .toList();
    }

    @Override
    public List<FuelLogsDomain> findLastFourByVehicleId(Long vehicleId) {
        return fuelLogsJpa.findTop4ByVehicleIdOrderByFilledAtDescIdDesc(vehicleId).stream()
                .map(FuelLogsMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<FuelLogsDomain> findById(Long id) {
        return fuelLogsJpa.findById(id)
                .map(FuelLogsMapper::toDomain);
    }

    @Override
    public boolean existsMoreRecentFuelLog(Long vehicleId, LocalDateTime filledAt) {
        return fuelLogsJpa.existsByVehicleIdAndFilledAtAfter(vehicleId, filledAt);
    }

    @Override
    public Optional<FuelLogsDomain> findLatestByVehicleId(Long vehicleId) {
        return fuelLogsJpa.findFirstByVehicleIdOrderByFilledAtDescIdDesc(vehicleId)
                .map(FuelLogsMapper::toDomain);
    }

    @Override
    public Page<FuelLogTankHistoryDomain> findTankHistoryByVehicleId(Long vehicleId, Pageable pageable) {
        return fuelLogsJpa.findTankHistoryByVehicleId(vehicleId, pageable)
                .map(FuelLogsMapper::toTankHistoryDomain);
    }

    @Override
    public BigDecimal sumPriceTotalByVehicleIdAndFilledAtBetween(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return fuelLogsJpa.sumPriceTotalByVehicleIdAndFilledAtBetween(vehicleId, startDate, endDate);
    }

    @Override
    public List<FuelLogEfficiencyDomain> findLatestTwoByVehicleId(Long vehicleId) {
        return fuelLogsJpa.findLatestTwoByVehicleId(vehicleId).stream()
                .map(this::toFuelLogEfficiencyDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        fuelLogsJpa.deleteById(id);
    }

    private FuelLogEfficiencyDomain toFuelLogEfficiencyDomain(FuelLogEfficiencyProjection projection) {
        return new FuelLogEfficiencyDomain(
                projection.getKmAtFill(),
                projection.getGallons(),
                projection.getFilledAt()
        );
    }
}
