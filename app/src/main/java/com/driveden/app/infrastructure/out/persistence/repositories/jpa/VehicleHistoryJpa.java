package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.driveden.app.infrastructure.out.persistence.entity.RepairEntity;
import com.driveden.app.infrastructure.out.persistence.projection.VehicleHistoryProjection;

public interface VehicleHistoryJpa extends Repository<RepairEntity, Long> {

    @Query(value = """
        SELECT *
        FROM (
            SELECT
                'REPAIR' AS type,
                r.id AS "eventId",
                COALESCE(NULLIF(TRIM(r.description), ''), MIN(p.name), 'Repair') AS title,
                COALESCE(r.total_cost, 0) AS amount,
                r.repair_date AS "eventDate"
            FROM repairs r
            LEFT JOIN repair_parts rp ON rp.repair_id = r.id
            LEFT JOIN parts p ON p.id = rp.part_id
            WHERE r.vehicle_id = :vehicleId
            GROUP BY r.id, r.description, r.total_cost, r.repair_date

            UNION ALL

            SELECT
                'FUEL' AS type,
                fl.id AS "eventId",
                'Fuel log' AS title,
                COALESCE(fl.price_total, 0) AS amount,
                fl.filled_at AS "eventDate"
            FROM fuel_logs fl
            WHERE fl.vehicle_id = :vehicleId
        ) vehicle_history
        ORDER BY "eventDate" DESC, "eventId" DESC
    """, countQuery = """
        SELECT COUNT(*)
        FROM (
            SELECT r.id
            FROM repairs r
            WHERE r.vehicle_id = :vehicleId

            UNION ALL

            SELECT fl.id
            FROM fuel_logs fl
            WHERE fl.vehicle_id = :vehicleId
        ) vehicle_history_count
    """, nativeQuery = true)
    Page<VehicleHistoryProjection> findByVehicleId(
            @Param("vehicleId") Long vehicleId,
            Pageable pageable
    );
}
