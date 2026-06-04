package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.fuelLogs.dto.FuelLogResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.FuelLogHistoryResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.FuelLogTankHistoryResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.LastFuelLogResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.RegisterFuelLogDTO;
import com.driveden.app.domain.fuelLogs.model.FuelLogTankHistoryDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;
import com.driveden.app.infrastructure.out.persistence.entity.FuelLogsEntity;
import com.driveden.app.infrastructure.out.persistence.projection.FuelLogTankHistoryProjection;

public class FuelLogsMapper {

    public static FuelLogsDomain toDomain(FuelLogsEntity entity) {
        if (entity == null) {
            return null;
        }

        return new FuelLogsDomain(
                entity.getId(),
                entity.getPriceTotal(),
                entity.getPricePerGallon(),
                entity.getGallons(),
                entity.getKmAtFill(),
                entity.getFilledAt(),
                entity.getGasStation(),
                entity.getVehicleId(),
                entity.getPaymentMethodId(),
                entity.getNotes()
        );
    }

    public static FuelLogsEntity toEntity(FuelLogsDomain domain) {
        if (domain == null) {
            return null;
        }

        FuelLogsEntity entity = new FuelLogsEntity();
        entity.setId(domain.getId());
        entity.setPriceTotal(domain.getPriceTotal());
        entity.setPricePerGallon(domain.getPricePerGallon());
        entity.setGallons(domain.getGallons());
        entity.setKmAtFill(domain.getKmAtFill());
        entity.setFilledAt(domain.getFilledAt());
        entity.setGasStation(domain.getGasStation());
        entity.setVehicleId(domain.getVehicleId());
        entity.setPaymentMethodId(domain.getPaymentMethodId());
        entity.setNotes(domain.getNotes());
        return entity;
    }

    public static FuelLogsDomain fromDTOtoDomain(RegisterFuelLogDTO dto) {
        if (dto == null) {
            return null;
        }

        return new FuelLogsDomain(
                null,
                dto.getPriceTotal(),
                dto.getPricePerGallon(),
                dto.getGallons(),
                dto.getKmAtFill(),
                dto.getFilledAt(),
                dto.getGasStation(),
                dto.getVehicleId(),
                dto.getPaymentMethodId(),
                dto.getNotes()
        );
    }

    public static FuelLogResponseDTO toResponseDTO(FuelLogsDomain domain) {
        if (domain == null) {
            return null;
        }

        return new FuelLogResponseDTO(
                domain.getId(),
                domain.getVehicleId(),
                domain.getGallons(),
                domain.getPriceTotal(),
                domain.getPricePerGallon(),
                domain.getKmAtFill(),
                domain.getFilledAt(),
                domain.getGasStation(),
                domain.getPaymentMethodId(),
                domain.getNotes()
        );
    }

    public static FuelLogHistoryResponseDTO toHistoryResponseDTO(FuelLogsDomain domain) {
        if (domain == null) {
            return null;
        }

        return new FuelLogHistoryResponseDTO(
                domain.getFilledAt().toLocalDate(),
                domain.getPriceTotal(),
                domain.getGallons()
        );
    }

    public static LastFuelLogResponseDTO toLastFuelLogResponseDTO(FuelLogsDomain domain) {
        if (domain == null) {
            return null;
        }

        return new LastFuelLogResponseDTO(
                domain.getFilledAt().toLocalDate(),
                domain.getPriceTotal(),
                domain.getGallons(),
                domain.getKmAtFill()
        );
    }

    public static FuelLogTankHistoryResponseDTO toTankHistoryResponseDTO(FuelLogTankHistoryProjection projection) {
        if (projection == null) {
            return null;
        }

        return toTankHistoryResponseDTO(toTankHistoryDomain(projection));
    }

    public static FuelLogTankHistoryDomain toTankHistoryDomain(FuelLogTankHistoryProjection projection) {
        if (projection == null) {
            return null;
        }

        return new FuelLogTankHistoryDomain(
                projection.getNotes(),
                projection.getGasStation(),
                projection.getPriceTotal()
        );
    }

    public static FuelLogTankHistoryResponseDTO toTankHistoryResponseDTO(FuelLogTankHistoryDomain domain) {
        if (domain == null) {
            return null;
        }

        return new FuelLogTankHistoryResponseDTO(
                domain.notes(),
                domain.gasStation(),
                domain.priceTotal()
        );
    }
}
