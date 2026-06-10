package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.FuelLogsEntity;
import com.driveden.app.infrastructure.out.persistence.projection.FuelLogEfficiencyProjection;
import com.driveden.app.infrastructure.out.persistence.projection.FuelLogTankHistoryProjection;

public interface FuelLogsJpa extends JpaRepository<FuelLogsEntity, Long> {

    List<FuelLogsEntity> findByVehicleIdOrderByFilledAtDesc(Long vehicleId);

    List<FuelLogsEntity> findByVehicleIdAndFilledAtGreaterThanEqualAndFilledAtLessThanEqualOrderByFilledAtAsc(
            Long vehicleId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<FuelLogsEntity> findTop4ByVehicleIdOrderByFilledAtDescIdDesc(Long vehicleId);

    boolean existsByVehicleIdAndFilledAtAfter(Long vehicleId, LocalDateTime filledAt);

    Optional<FuelLogsEntity> findFirstByVehicleIdOrderByFilledAtDescIdDesc(Long vehicleId);

    @Query("""
        SELECT FL.id AS id,
               FL.notes AS notes,
               FL.gasStation AS gasStation,
               FL.priceTotal AS priceTotal
        FROM FuelLogsEntity FL
        WHERE FL.vehicleId = :vehicleId
    """)
    Page<FuelLogTankHistoryProjection> findTankHistoryByVehicleId(
            @Param("vehicleId") Long vehicleId,
            Pageable pageable
    );

    @Query("""
        SELECT COALESCE(SUM(FL.priceTotal), 0)
        FROM FuelLogsEntity FL
        WHERE FL.vehicleId = :vehicleId
        AND FL.filledAt >= :startDate
        AND FL.filledAt < :endDate
    """)
    BigDecimal sumPriceTotalByVehicleIdAndFilledAtBetween(
            @Param("vehicleId") Long vehicleId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query(value = """
        SELECT
            km_at_fill AS "kmAtFill",
            gallons AS gallons,
            filled_at AS "filledAt"
        FROM fuel_logs
        WHERE vehicle_id = :vehicleId
        ORDER BY filled_at DESC, id DESC
        LIMIT 2
    """, nativeQuery = true)
    List<FuelLogEfficiencyProjection> findLatestTwoByVehicleId(@Param("vehicleId") Long vehicleId);
}
