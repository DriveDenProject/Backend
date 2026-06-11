package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationDomain;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleNotificationMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.VehicleNotificationJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VehicleNotificationRepository {

    private final VehicleNotificationJpa vehicleNotificationJpa;

    public VehicleNotificationDomain save(VehicleNotificationDomain vehicleNotificationDomain) {
        return VehicleNotificationMapper.toDomain(
                vehicleNotificationJpa.save(
                        VehicleNotificationMapper.toEntity(vehicleNotificationDomain)
                )
        );
    }

    public List<VehicleNotificationDomain> findAllByUserId(Long userId) {
        return vehicleNotificationJpa.findAllByUserId(userId).stream()
                .map(VehicleNotificationMapper::toDomain)
                .toList();
    }

    public Optional<VehicleNotificationDomain> findLatestByUserId(Long userId) {
        return vehicleNotificationJpa.findLatestByUserId(userId, PageRequest.of(0, 1)).stream()
                .findFirst()
                .map(VehicleNotificationMapper::toDomain);
    }

    public List<VehicleNotificationDomain> findByVehicleId(Long vehicleId) {
        return vehicleNotificationJpa.findByVehicleIdOrderByDueDateAsc(vehicleId).stream()
                .map(VehicleNotificationMapper::toDomain)
                .toList();
    }

    public Optional<VehicleNotificationDomain> findNextPendingByVehicleId(Long vehicleId) {
        return vehicleNotificationJpa
                .findNextByVehicleIdAndStatus(vehicleId, VehicleNotificationStatus.PENDING, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(VehicleNotificationMapper::toDomain);
    }

    public Optional<VehicleNotificationDomain> findById(Long id) {
        return vehicleNotificationJpa.findById(id)
                .map(VehicleNotificationMapper::toDomain);
    }

    public List<VehicleNotificationDomain> findPendingOverdue(LocalDate today) {
        return vehicleNotificationJpa.findByStatusAndDueDateBefore(VehicleNotificationStatus.PENDING, today).stream()
                .map(VehicleNotificationMapper::toDomain)
                .toList();
    }

    public List<VehicleNotificationDomain> findPendingReadyToDispatch(LocalDate today, LocalDateTime now) {
        return vehicleNotificationJpa.findPendingReadyToDispatch(today, now).stream()
                .map(VehicleNotificationMapper::toDomain)
                .toList();
    }

    public void deleteById(Long id) {
        vehicleNotificationJpa.deleteById(id);
    }
}
