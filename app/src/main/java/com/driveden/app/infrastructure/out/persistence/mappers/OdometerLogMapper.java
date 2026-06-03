package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.odometerLogs.model.OdometerLogDomain;
import com.driveden.app.infrastructure.out.persistence.entity.OdometerLogEntity;

public class OdometerLogMapper {

    public static OdometerLogDomain toDomain(OdometerLogEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OdometerLogDomain(
                entity.getId(),
                entity.getVehicleId(),
                entity.getKm(),
                entity.getRecordedAt(),
                entity.getNote(),
                entity.getSource(),
                entity.getSourceId(),
                entity.getCreatedAt()
        );
    }

    public static OdometerLogEntity toEntity(OdometerLogDomain domain) {
        if (domain == null) {
            return null;
        }

        OdometerLogEntity entity = new OdometerLogEntity();
        entity.setId(domain.getId());
        entity.setVehicleId(domain.getVehicleId());
        entity.setKm(domain.getKm());
        entity.setRecordedAt(domain.getRecordedAt());
        entity.setNote(domain.getNote());
        entity.setSource(domain.getSource());
        entity.setSourceId(domain.getSourceId());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }
}
