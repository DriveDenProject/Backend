package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.vehicleNotifications.dto.RegisterVehicleNotificationDTO;
import com.driveden.app.domain.vehicleNotifications.dto.VehicleNotificationResponseDTO;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationDomain;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleNotificationEntity;

public class VehicleNotificationMapper {

    public static VehicleNotificationDomain toDomain(VehicleNotificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return new VehicleNotificationDomain(
                entity.getId(),
                entity.getVehicleId(),
                entity.getCategoryId(),
                entity.getServiceName(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getDueDate(),
                entity.getReminderFrequencyDays(),
                entity.getNotifyBeforeDays(),
                entity.getPriority(),
                entity.getStatus(),
                entity.getIsRecurring(),
                entity.getRecurrenceIntervalDays(),
                entity.getLastNotificationSent(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static VehicleNotificationEntity toEntity(VehicleNotificationDomain domain) {
        if (domain == null) {
            return null;
        }

        VehicleNotificationEntity entity = new VehicleNotificationEntity();
        entity.setId(domain.getId());
        entity.setVehicleId(domain.getVehicleId());
        entity.setCategoryId(domain.getCategoryId());
        entity.setServiceName(domain.getServiceName());
        entity.setDescription(domain.getDescription());
        entity.setStartDate(domain.getStartDate());
        entity.setDueDate(domain.getDueDate());
        entity.setReminderFrequencyDays(domain.getReminderFrequencyDays());
        entity.setNotifyBeforeDays(domain.getNotifyBeforeDays());
        entity.setPriority(domain.getPriority());
        entity.setStatus(domain.getStatus());
        entity.setIsRecurring(domain.getIsRecurring());
        entity.setRecurrenceIntervalDays(domain.getRecurrenceIntervalDays());
        entity.setLastNotificationSent(domain.getLastNotificationSent());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    public static VehicleNotificationDomain fromDTOtoDomain(RegisterVehicleNotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        return new VehicleNotificationDomain(
                null,
                dto.getVehicleId(),
                dto.getCategoryId(),
                dto.getServiceName(),
                dto.getDescription(),
                dto.getStartDate(),
                dto.getDueDate(),
                dto.getReminderFrequencyDays(),
                dto.getNotifyBeforeDays(),
                dto.getPriority(),
                VehicleNotificationStatus.PENDING,
                dto.getIsRecurring(),
                dto.getRecurrenceIntervalDays(),
                null,
                null,
                null
        );
    }

    public static VehicleNotificationResponseDTO toResponseDTO(VehicleNotificationDomain domain) {
        if (domain == null) {
            return null;
        }

        return new VehicleNotificationResponseDTO(
                domain.getId(),
                domain.getVehicleId(),
                domain.getCategoryId(),
                domain.getServiceName(),
                domain.getDescription(),
                domain.getStartDate(),
                domain.getDueDate(),
                domain.getReminderFrequencyDays(),
                domain.getNotifyBeforeDays(),
                domain.getPriority(),
                domain.getStatus(),
                domain.getIsRecurring(),
                domain.getRecurrenceIntervalDays(),
                domain.getLastNotificationSent(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
