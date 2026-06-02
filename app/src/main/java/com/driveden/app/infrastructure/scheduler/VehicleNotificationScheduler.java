package com.driveden.app.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.driveden.app.application.services.VehicleNotificationSchedulerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleNotificationScheduler {

    private final VehicleNotificationSchedulerService vehicleNotificationSchedulerService;

    @Scheduled(cron = "0 0 * * * *")
    public void processVehicleNotifications() {
        vehicleNotificationSchedulerService.processPendingNotifications();
    }
}
