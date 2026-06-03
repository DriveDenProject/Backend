package com.driveden.app.application.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.PushNotificationPort;
import com.driveden.app.domain.cars.model.vehicleDomain;
import com.driveden.app.domain.deviceTokens.model.PushNotificationResult;
import com.driveden.app.domain.deviceTokens.model.UserDeviceTokenDomain;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationDomain;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserDeviceTokenRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationDispatchService {

    private static final String VEHICLE_REMINDER_TITLE = "Vehicle Reminder";

    private final PushNotificationPort pushNotificationPort;
    private final UserDeviceTokenRepository userDeviceTokenRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final VehicleRepository vehicleRepository;
    private final DeviceTokenService deviceTokenService;

    public boolean dispatch(VehicleNotificationDomain vehicleNotificationDomain) {
        vehicleDomain vehicleDomain = vehicleRepository.findById(vehicleNotificationDomain.getVehicleId())
                .orElse(null);

        if (vehicleDomain == null) {
            log.warn("Vehicle notification dispatch skipped. Vehicle not found notificationId={} vehicleId={}",
                    vehicleNotificationDomain.getId(),
                    vehicleNotificationDomain.getVehicleId());
            return false;
        }

        Map<String, String> data = Map.of(
                "vehicleId", String.valueOf(vehicleNotificationDomain.getVehicleId()),
                "notificationId", String.valueOf(vehicleNotificationDomain.getId())
        );

        String body = buildBody(vehicleNotificationDomain, vehicleDomain);

        boolean anySuccessful = false;

        for (Long userId : userVehicleRepository.findUserIdsByVehicleId(vehicleNotificationDomain.getVehicleId())) {
            for (UserDeviceTokenDomain deviceToken : userDeviceTokenRepository.findActiveByUserId(userId)) {
                PushNotificationResult result = pushNotificationPort.send(
                        deviceToken.getFcmToken(),
                        VEHICLE_REMINDER_TITLE,
                        body,
                        data
                );

                if (Boolean.TRUE.equals(result.successful())) {
                    anySuccessful = true;
                    log.info("Vehicle notification push sent notificationId={} vehicleId={} userId={} tokenId={} providerMessageId={}",
                            vehicleNotificationDomain.getId(),
                            vehicleNotificationDomain.getVehicleId(),
                            userId,
                            deviceToken.getId(),
                            result.providerMessageId());
                    continue;
                }

                if (Boolean.TRUE.equals(result.invalidToken())) {
                    deviceTokenService.deactivateDeviceToken(deviceToken.getFcmToken());
                }
            }
        }

        return anySuccessful;
    }

    private String buildBody(VehicleNotificationDomain vehicleNotificationDomain, vehicleDomain vehicleDomain) {
        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), vehicleNotificationDomain.getDueDate());
        String vehicleName = vehicleDomain.getBrand() + " " + vehicleDomain.getModel();

        if (daysUntilDue == 0) {
            return vehicleNotificationDomain.getServiceName() + " for " + vehicleName + " is due today";
        }

        if (daysUntilDue == 1) {
            return vehicleNotificationDomain.getServiceName() + " for " + vehicleName + " is due tomorrow";
        }

        return vehicleNotificationDomain.getServiceName() + " for " + vehicleName
                + " is due in " + daysUntilDue + " days";
    }
}
