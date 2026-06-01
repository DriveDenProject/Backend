package com.driveden.app.application.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.cars.model.vehicleDetailsDomain;
import com.driveden.app.domain.fuelLogs.model.FuelLogsDomain;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.FuelLogsRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleDetailsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleOdometerService {

    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final FuelLogsRepository fuelLogsRepository;

    public void validateOdometer(Long vehicleId, Integer kmAtFill) {
        vehicleDetailsDomain vehicleDetails = findVehicleDetails(vehicleId);

        if (kmAtFill <= vehicleDetails.getCurrentKm()) {
            throw new CustomException(
                    "The odometer must be greater than the vehicle current mileage",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void validateOdometerForUpdate(Long vehicleId, Integer kmAtFill, FuelLogsDomain currentFuelLog) {
        vehicleDetailsDomain vehicleDetails = findVehicleDetails(vehicleId);

        if (currentFuelLog.getKmAtFill().equals(vehicleDetails.getCurrentKm()) && kmAtFill <= 0) {
            throw new CustomException(
                    "The odometer must be greater than zero",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void updateCurrentKm(Long vehicleId, Integer currentKm) {
        findVehicleDetails(vehicleId);
        vehicleDetailsRepository.updateCurrentKm(vehicleId, currentKm);
    }

    public void updateCurrentKmIfLatestFuelLog(FuelLogsDomain fuelLog) {
        boolean hasMoreRecentFuelLog = fuelLogsRepository.existsMoreRecentFuelLog(
                fuelLog.getVehicleId(),
                fuelLog.getFilledAt()
        );

        if (!hasMoreRecentFuelLog) {
            updateCurrentKm(fuelLog.getVehicleId(), fuelLog.getKmAtFill());
        }
    }

    public void recalculateCurrentKm(Long vehicleId) {
        fuelLogsRepository.findLatestByVehicleId(vehicleId)
                .ifPresent(fuelLog -> updateCurrentKm(vehicleId, fuelLog.getKmAtFill()));
    }

    private vehicleDetailsDomain findVehicleDetails(Long vehicleId) {
        return vehicleDetailsRepository.findByVehicleId(vehicleId)
                .orElseThrow(() -> new CustomException("Vehicle details not found", HttpStatus.NOT_FOUND));
    }

}
