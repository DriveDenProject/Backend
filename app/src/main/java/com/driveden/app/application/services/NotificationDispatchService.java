package com.driveden.app.application.services;

import org.springframework.stereotype.Service;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationDomain;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationDispatchService {

    public void dispatch(VehicleNotificationDomain vehicleNotificationDomain) {
        log.info(
                "Vehicle notification dispatched notificationId={} vehicleId={} categoryId={} serviceName={} dueDate={} priority={}",
                vehicleNotificationDomain.getId(),
                vehicleNotificationDomain.getVehicleId(),
                vehicleNotificationDomain.getCategoryId(),
                vehicleNotificationDomain.getServiceName(),
                vehicleNotificationDomain.getDueDate(),
                vehicleNotificationDomain.getPriority()
        );
    }
}
