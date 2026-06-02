package com.driveden.app.application.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.vehicleNotifications.dto.RegisterVehicleNotificationDTO;
import com.driveden.app.domain.vehicleNotifications.dto.UpdateVehicleNotificationDTO;
import com.driveden.app.domain.vehicleNotifications.dto.VehicleNotificationResponseDTO;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationDomain;
import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationStatus;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleNotificationMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.MaintenanceCategoryRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleNotificationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleNotificationService {

    private final VehicleNotificationRepository vehicleNotificationRepository;
    private final MaintenanceCategoryRepository maintenanceCategoryRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final UsersService usersService;

    @Transactional
    public VehicleNotificationResponseDTO registerVehicleNotification(
            RegisterVehicleNotificationDTO registerVehicleNotificationDTO,
            Long userId
    ) {
        validateVehicleOwnership(userId, registerVehicleNotificationDTO.getVehicleId());
        validateCategory(registerVehicleNotificationDTO.getCategoryId());
        validateDates(registerVehicleNotificationDTO.getStartDate(), registerVehicleNotificationDTO.getDueDate());
        validateRecurrence(
                registerVehicleNotificationDTO.getIsRecurring(),
                registerVehicleNotificationDTO.getRecurrenceIntervalDays()
        );

        VehicleNotificationDomain vehicleNotificationDomain = VehicleNotificationMapper.fromDTOtoDomain(
                registerVehicleNotificationDTO
        );

        return VehicleNotificationMapper.toResponseDTO(
                vehicleNotificationRepository.save(vehicleNotificationDomain)
        );
    }

    public List<VehicleNotificationResponseDTO> getVehicleNotifications(Long userId) {
        usersService.findUserById(userId);

        return vehicleNotificationRepository.findAllByUserId(userId).stream()
                .map(VehicleNotificationMapper::toResponseDTO)
                .toList();
    }

    public List<VehicleNotificationResponseDTO> getVehicleNotificationsByVehicle(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        return vehicleNotificationRepository.findByVehicleId(vehicleId).stream()
                .map(VehicleNotificationMapper::toResponseDTO)
                .toList();
    }

    public VehicleNotificationResponseDTO getVehicleNotification(Long notificationId, Long userId) {
        VehicleNotificationDomain vehicleNotificationDomain = findVehicleNotificationById(notificationId);
        validateVehicleOwnership(userId, vehicleNotificationDomain.getVehicleId());

        return VehicleNotificationMapper.toResponseDTO(vehicleNotificationDomain);
    }

    @Transactional
    public VehicleNotificationResponseDTO updateVehicleNotification(
            Long notificationId,
            UpdateVehicleNotificationDTO updateVehicleNotificationDTO,
            Long userId
    ) {
        VehicleNotificationDomain currentVehicleNotification = findVehicleNotificationById(notificationId);
        validateVehicleOwnership(userId, currentVehicleNotification.getVehicleId());
        validateCategory(updateVehicleNotificationDTO.getCategoryId());
        validateDates(updateVehicleNotificationDTO.getStartDate(), updateVehicleNotificationDTO.getDueDate());
        validateRecurrence(
                updateVehicleNotificationDTO.getIsRecurring(),
                updateVehicleNotificationDTO.getRecurrenceIntervalDays()
        );

        VehicleNotificationDomain updatedVehicleNotification = new VehicleNotificationDomain(
                currentVehicleNotification.getId(),
                currentVehicleNotification.getVehicleId(),
                updateVehicleNotificationDTO.getCategoryId(),
                updateVehicleNotificationDTO.getServiceName(),
                updateVehicleNotificationDTO.getDescription(),
                updateVehicleNotificationDTO.getStartDate(),
                updateVehicleNotificationDTO.getDueDate(),
                updateVehicleNotificationDTO.getReminderFrequencyDays(),
                updateVehicleNotificationDTO.getNotifyBeforeDays(),
                updateVehicleNotificationDTO.getPriority(),
                currentVehicleNotification.getStatus(),
                updateVehicleNotificationDTO.getIsRecurring(),
                updateVehicleNotificationDTO.getRecurrenceIntervalDays(),
                currentVehicleNotification.getLastNotificationSent(),
                currentVehicleNotification.getCreatedAt(),
                currentVehicleNotification.getUpdatedAt()
        );

        return VehicleNotificationMapper.toResponseDTO(
                vehicleNotificationRepository.save(updatedVehicleNotification)
        );
    }

    @Transactional
    public String deleteVehicleNotification(Long notificationId, Long userId) {
        VehicleNotificationDomain vehicleNotificationDomain = findVehicleNotificationById(notificationId);
        validateVehicleOwnership(userId, vehicleNotificationDomain.getVehicleId());

        vehicleNotificationRepository.deleteById(notificationId);

        return "Vehicle notification deleted successfully";
    }

    @Transactional
    public VehicleNotificationResponseDTO completeVehicleNotification(Long notificationId, Long userId) {
        VehicleNotificationDomain vehicleNotificationDomain = findVehicleNotificationById(notificationId);
        validateVehicleOwnership(userId, vehicleNotificationDomain.getVehicleId());

        if (Boolean.TRUE.equals(vehicleNotificationDomain.getIsRecurring())) {
            if (vehicleNotificationDomain.getRecurrenceIntervalDays() == null
                    || vehicleNotificationDomain.getRecurrenceIntervalDays() <= 0) {
                throw new CustomException("Recurring notification interval is invalid", HttpStatus.BAD_REQUEST);
            }

            vehicleNotificationDomain.setStartDate(vehicleNotificationDomain.getDueDate());
            vehicleNotificationDomain.setDueDate(
                    vehicleNotificationDomain.getDueDate().plusDays(vehicleNotificationDomain.getRecurrenceIntervalDays())
            );
            vehicleNotificationDomain.setStatus(VehicleNotificationStatus.PENDING);
            vehicleNotificationDomain.setLastNotificationSent(null);
        } else {
            vehicleNotificationDomain.setStatus(VehicleNotificationStatus.COMPLETED);
        }

        return VehicleNotificationMapper.toResponseDTO(
                vehicleNotificationRepository.save(vehicleNotificationDomain)
        );
    }

    @Transactional
    public VehicleNotificationResponseDTO updateVehicleNotificationStatus(
            Long notificationId,
            VehicleNotificationStatus status,
            Long userId
    ) {
        VehicleNotificationDomain vehicleNotificationDomain = findVehicleNotificationById(notificationId);
        validateVehicleOwnership(userId, vehicleNotificationDomain.getVehicleId());
        vehicleNotificationDomain.setStatus(status);

        return VehicleNotificationMapper.toResponseDTO(
                vehicleNotificationRepository.save(vehicleNotificationDomain)
        );
    }

    private VehicleNotificationDomain findVehicleNotificationById(Long notificationId) {
        return vehicleNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException("Vehicle notification not found", HttpStatus.NOT_FOUND));
    }

    private void validateVehicleOwnership(Long userId, Long vehicleId) {
        usersService.findUserById(userId);

        if (!userVehicleRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new CustomException("Vehicle not found for user", HttpStatus.NOT_FOUND);
        }
    }

    private void validateCategory(Long categoryId) {
        if (!maintenanceCategoryRepository.existsById(categoryId)) {
            throw new CustomException("Maintenance category not found", HttpStatus.NOT_FOUND);
        }
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate dueDate) {
        if (dueDate.isBefore(startDate)) {
            throw new CustomException("dueDate must be greater than or equal to startDate", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateRecurrence(Boolean isRecurring, Integer recurrenceIntervalDays) {
        if (Boolean.TRUE.equals(isRecurring)) {
            if (recurrenceIntervalDays == null || recurrenceIntervalDays <= 0) {
                throw new CustomException("recurrenceIntervalDays is required and must be greater than 0", HttpStatus.BAD_REQUEST);
            }
            return;
        }

        if (recurrenceIntervalDays != null) {
            throw new CustomException("recurrenceIntervalDays must be null when isRecurring is false", HttpStatus.BAD_REQUEST);
        }
    }
}
