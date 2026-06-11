package com.driveden.app.infrastructure.controllers.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.VehicleNotificationService;
import com.driveden.app.domain.auth.dto.AuthenticatedUser;
import com.driveden.app.domain.vehicleNotifications.dto.NotificationDispatchResponseDTO;
import com.driveden.app.domain.vehicleNotifications.dto.RegisterVehicleNotificationDTO;
import com.driveden.app.domain.vehicleNotifications.dto.UpdateVehicleNotificationDTO;
import com.driveden.app.domain.vehicleNotifications.dto.UpdateVehicleNotificationStatusDTO;
import com.driveden.app.domain.vehicleNotifications.dto.VehicleNotificationResponseDTO;
import com.driveden.app.utils.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle-notifications")
@Validated
public class VehicleNotificationsController {

    private final VehicleNotificationService vehicleNotificationService;

    @PostMapping
    public CustomResponse<VehicleNotificationResponseDTO> registerVehicleNotification(
            @Valid @RequestBody RegisterVehicleNotificationDTO registerVehicleNotificationDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.registerVehicleNotification(registerVehicleNotificationDTO, authenticatedUser.id()),
                HttpStatus.CREATED,
                "Vehicle notification registered successfully"
        );
    }

    @GetMapping
    public CustomResponse<List<VehicleNotificationResponseDTO>> getVehicleNotifications(
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.getVehicleNotifications(authenticatedUser.id()),
                HttpStatus.OK,
                "Vehicle notifications retrieved successfully"
        );
    }

    @GetMapping("/vehicle")
    public CustomResponse<List<VehicleNotificationResponseDTO>> getVehicleNotificationsByVehicle(
            @RequestParam Long vehicleId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.getVehicleNotificationsByVehicle(vehicleId, authenticatedUser.id()),
                HttpStatus.OK,
                "Vehicle notifications retrieved successfully"
        );
    }

    @GetMapping("/{notificationId}")
    public CustomResponse<VehicleNotificationResponseDTO> getVehicleNotification(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.getVehicleNotification(notificationId, authenticatedUser.id()),
                HttpStatus.OK,
                "Vehicle notification retrieved successfully"
        );
    }

    @PutMapping("/{notificationId}")
    public CustomResponse<VehicleNotificationResponseDTO> updateVehicleNotification(
            @PathVariable Long notificationId,
            @Valid @RequestBody UpdateVehicleNotificationDTO updateVehicleNotificationDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.updateVehicleNotification(
                        notificationId,
                        updateVehicleNotificationDTO,
                        authenticatedUser.id()
                ),
                HttpStatus.OK,
                "Vehicle notification updated successfully"
        );
    }

    @DeleteMapping("/{notificationId}")
    public CustomResponse<String> deleteVehicleNotification(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.deleteVehicleNotification(notificationId, authenticatedUser.id()),
                HttpStatus.OK,
                "Vehicle notification deleted successfully"
        );
    }

    @PatchMapping("/{notificationId}/complete")
    public CustomResponse<VehicleNotificationResponseDTO> completeVehicleNotification(
            @PathVariable Long notificationId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.completeVehicleNotification(notificationId, authenticatedUser.id()),
                HttpStatus.OK,
                "Vehicle notification completed successfully"
        );
    }

    @PatchMapping("/{notificationId}/status")
    public CustomResponse<VehicleNotificationResponseDTO> updateVehicleNotificationStatus(
            @PathVariable Long notificationId,
            @Valid @RequestBody UpdateVehicleNotificationStatusDTO updateVehicleNotificationStatusDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.updateVehicleNotificationStatus(
                        notificationId,
                        updateVehicleNotificationStatusDTO.getStatus(),
                        authenticatedUser.id()
                ),
                HttpStatus.OK,
                "Vehicle notification status updated successfully"
        );
    }

    @PostMapping("/latest/dispatch")
    public CustomResponse<NotificationDispatchResponseDTO> dispatchLatestVehicleNotification(
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                vehicleNotificationService.dispatchLatestVehicleNotification(authenticatedUser.id()),
                HttpStatus.OK,
                "Latest vehicle notification dispatch processed"
        );
    }
}
