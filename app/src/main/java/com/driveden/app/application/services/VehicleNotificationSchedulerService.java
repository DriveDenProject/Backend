package com.driveden.app.application.services;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationDomain;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleNotificationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleNotificationSchedulerService {

    private final VehicleNotificationRepository vehicleNotificationRepository;
    private final NotificationDispatchService notificationDispatchService;

    @Transactional
    public void processPendingNotifications() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        vehicleNotificationRepository.findPendingOverdue(today)
                .forEach(this::markAsOverdue);

        vehicleNotificationRepository.findPendingReadyToDispatch(today, now)
                .forEach(notification -> dispatchNotification(notification, now));
    }

    private void markAsOverdue(VehicleNotificationDomain vehicleNotificationDomain) {
        vehicleNotificationDomain.setStatus(VehicleNotificationStatus.OVERDUE);
        vehicleNotificationRepository.save(vehicleNotificationDomain);
    }

    private void dispatchNotification(VehicleNotificationDomain vehicleNotificationDomain, LocalDateTime now) {
        if (notificationDispatchService.dispatch(vehicleNotificationDomain)) {
            vehicleNotificationDomain.setLastNotificationSent(now);
            vehicleNotificationRepository.save(vehicleNotificationDomain);
        }
    }
}
