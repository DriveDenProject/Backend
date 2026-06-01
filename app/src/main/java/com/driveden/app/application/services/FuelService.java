package com.driveden.app.application.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.fuelLogs.dto.FuelLogResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.RegisterFuelLogDTO;
import com.driveden.app.domain.fuelLogs.dto.UpdateFuelLogDTO;
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
        vehicleOdometerService.updateCurrentKmIfLatestFuelLog(savedFuelLog);

        return FuelLogsMapper.toResponseDTO(savedFuelLog);
    }

    @Transactional
    public FuelLogResponseDTO updateFuelLog(Long fuelLogId, UpdateFuelLogDTO updateFuelLogDTO, Long userId) {
        FuelLogsDomain currentFuelLog = findFuelLogById(fuelLogId);
        validateVehicleOwnership(userId, currentFuelLog.getVehicleId());
        validatePaymentMethod(updateFuelLogDTO.getPaymentMethodId());
        vehicleOdometerService.validateOdometerForUpdate(
                currentFuelLog.getVehicleId(),
                updateFuelLogDTO.getKmAtFill(),
                currentFuelLog
        );

        FuelLogsDomain updatedFuelLog = new FuelLogsDomain(
                currentFuelLog.getId(),
                updateFuelLogDTO.getPriceTotal(),
                updateFuelLogDTO.getPricePerGallon(),
                updateFuelLogDTO.getGallons(),
                updateFuelLogDTO.getKmAtFill(),
                updateFuelLogDTO.getFilledAt(),
                updateFuelLogDTO.getGasStation(),
                currentFuelLog.getVehicleId(),
                updateFuelLogDTO.getPaymentMethodId(),
                updateFuelLogDTO.getNotes()
        );

        FuelLogsDomain savedFuelLog = fuelLogsRepository.save(updatedFuelLog);
        vehicleOdometerService.recalculateCurrentKm(savedFuelLog.getVehicleId());

        return FuelLogsMapper.toResponseDTO(savedFuelLog);
    }

    @Transactional
    public String deleteFuelLog(Long fuelLogId, Long userId) {
        FuelLogsDomain fuelLog = findFuelLogById(fuelLogId);
        validateVehicleOwnership(userId, fuelLog.getVehicleId());

        fuelLogsRepository.deleteById(fuelLog.getId());
        vehicleOdometerService.recalculateCurrentKm(fuelLog.getVehicleId());

        return "Fuel log deleted successfully";
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

    private FuelLogsDomain findFuelLogById(Long fuelLogId) {
        return fuelLogsRepository.findById(fuelLogId)
                .orElseThrow(() -> new CustomException("Fuel log not found", HttpStatus.NOT_FOUND));
    }
}
