package com.driveden.app.application.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.fuelLogs.dto.FuelLogResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.RegisterFuelLogDTO;
import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.FuelLogsMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.FuelLogsRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.PaymentMethodRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FuelService {

    private final FuelLogsRepository fuelLogsRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final VehicleOdometerService vehicleOdometerService;
    private final UsersService usersService;

    @Transactional
    public FuelLogResponseDTO registerFuelLog(RegisterFuelLogDTO registerFuelLogDTO, Long userId) {
        validateVehicleOwnership(userId, registerFuelLogDTO.getVehicleId());
        validatePaymentMethod(registerFuelLogDTO.getPaymentMethodId());
        vehicleOdometerService.validateOdometer(registerFuelLogDTO.getVehicleId(), registerFuelLogDTO.getKmAtFill());

        FuelLogsDomain fuelLogsDomain = FuelLogsMapper.fromDTOtoDomain(registerFuelLogDTO);
        FuelLogsDomain savedFuelLog = fuelLogsRepository.save(fuelLogsDomain);
        updateVehicleOdometerIfLatestFuelLog(savedFuelLog);

        return FuelLogsMapper.toResponseDTO(savedFuelLog);
    }

    public List<FuelLogResponseDTO> getFuelLogs(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        return fuelLogsRepository.findByVehicleId(vehicleId).stream()
                .map(FuelLogsMapper::toResponseDTO)
                .toList();
    }

    private void validateVehicleOwnership(Long userId, Long vehicleId) {
        usersService.findUserById(userId);

        if (!userVehicleRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new CustomException("Vehicle not found for user", HttpStatus.NOT_FOUND);
        }
    }

    private void validatePaymentMethod(Long paymentMethodId) {
        if (paymentMethodId == null) {
            return;
        }

        if (!paymentMethodRepository.existsAvailableById(paymentMethodId)) {
            throw new CustomException("Payment method not found or inactive", HttpStatus.NOT_FOUND);
        }
    }

    private void updateVehicleOdometerIfLatestFuelLog(FuelLogsDomain savedFuelLog) {
        boolean hasMoreRecentFuelLog = fuelLogsRepository.existsMoreRecentFuelLog(
                savedFuelLog.getVehicleId(),
                savedFuelLog.getFilledAt()
        );

        if (!hasMoreRecentFuelLog) {
            vehicleOdometerService.updateCurrentKm(savedFuelLog.getVehicleId(), savedFuelLog.getKmAtFill());
        }
    }
}
