package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.vehicleHistory.dto.VehicleHistoryItemResponseDTO;
import com.driveden.app.domain.vehicleHistory.model.VehicleHistoryItemDomain;
import com.driveden.app.infrastructure.out.persistence.projection.VehicleHistoryProjection;

public class VehicleHistoryMapper {

    public static VehicleHistoryItemDomain toDomain(VehicleHistoryProjection projection) {
        if (projection == null) {
            return null;
        }

        return new VehicleHistoryItemDomain(
                projection.getType(),
                projection.getEventId(),
                projection.getTitle(),
                projection.getAmount(),
                projection.getEventDate().toLocalDate()
        );
    }

    public static VehicleHistoryItemResponseDTO toResponseDTO(VehicleHistoryItemDomain domain) {
        if (domain == null) {
            return null;
        }

        return new VehicleHistoryItemResponseDTO(
                domain.type(),
                domain.id(),
                domain.title(),
                domain.amount(),
                domain.date()
        );
    }
}
