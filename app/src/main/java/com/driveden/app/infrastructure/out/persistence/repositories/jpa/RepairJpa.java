package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.RepairEntity;
import com.driveden.app.infrastructure.out.persistence.projection.RepairHistoryProjection;
import com.driveden.app.infrastructure.out.persistence.projection.RepairStatsProjection;

public interface RepairJpa extends JpaRepository<RepairEntity, Long> {

    @Query(value = """
        SELECT
            r.id AS "repairId",
            COALESCE(NULLIF(TRIM(r.description), ''), MIN(p.name), 'Repair') AS "name",
            COALESCE(r.total_cost, 0) AS "cost",
            r.repair_date AS "repairDate"
        FROM repairs r
        LEFT JOIN repair_parts rp ON rp.repair_id = r.id
        LEFT JOIN parts p ON p.id = rp.part_id
        WHERE r.vehicle_id = :vehicleId
        GROUP BY r.id, r.description, r.total_cost, r.repair_date
        ORDER BY r.repair_date DESC, r.id DESC
    """, nativeQuery = true)
    List<RepairHistoryProjection> findHistoryByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query(value = """
        SELECT
            MAX(r.repair_date) AS "lastRepair",
            COALESCE(SUM(r.total_cost), 0) AS "totalSpent",
            COUNT(r.id) AS "totalRepairs"
        FROM repairs r
        WHERE r.vehicle_id = :vehicleId
    """, nativeQuery = true)
    RepairStatsProjection findStatsByVehicleId(@Param("vehicleId") Long vehicleId);
}
