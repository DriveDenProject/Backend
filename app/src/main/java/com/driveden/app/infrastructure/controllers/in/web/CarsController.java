package com.driveden.app.infrastructure.controllers.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.CarService;
import com.driveden.app.application.services.FuelService;
import com.driveden.app.application.services.OdometerMileageStatsService;
import com.driveden.app.domain.auth.dto.AuthenticatedUser;
import com.driveden.app.domain.cars.dto.carRegisterRequestDTO;
import com.driveden.app.domain.cars.dto.makesDTO;
import com.driveden.app.domain.cars.dto.modelByGenerationDTO;
import com.driveden.app.domain.cars.dto.modelsDTO;
import com.driveden.app.domain.cars.model.vehicleDomain;
import com.driveden.app.domain.fuelType.model.FuelTypeDomain;
import com.driveden.app.domain.fuelLogs.dto.FuelLogResponseDTO;
import com.driveden.app.domain.fuelLogs.dto.RegisterFuelLogDTO;
import com.driveden.app.domain.fuelLogs.dto.UpdateFuelLogDTO;
import com.driveden.app.domain.odometerLogs.dto.CurrentMonthMileageStatsResponseDTO;
import com.driveden.app.domain.odometerLogs.dto.MonthlyMileageStatsResponseDTO;
import com.driveden.app.domain.transmissionType.model.transmissionTypeDomain;
import com.driveden.app.utils.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
@Validated
public class CarsController {

    private final CarService carService;
    private final FuelService fuelService;
    private final OdometerMileageStatsService odometerMileageStatsService;

    @GetMapping("/all-makes")
    public CustomResponse<List<makesDTO>> getAllMakes() {

        return new CustomResponse<List<makesDTO>>(
            carService.getAllMakes(),
            HttpStatus.OK,
            "Car makes retrieved successfully"
        );

    }

    @GetMapping("/models")
    public CustomResponse<List<modelsDTO>> getModelsByMake(@RequestParam String makeId) {

        return new CustomResponse<List<modelsDTO>>(
            carService.getModelsByMake(makeId),
            HttpStatus.OK,
            "Car models retrieved successfully"
        );
    
    }

    @GetMapping("/models/generations")
    public CustomResponse<List<modelByGenerationDTO>> getModelsByGeneration(@RequestParam String modelId) {

        return new CustomResponse<List<modelByGenerationDTO>>(
            carService.getModelsByGeneration(modelId),
            HttpStatus.OK,
            "Car generations retrieved successfully"
        );

    }

    @GetMapping("/fuel-type")
    public CustomResponse<List<FuelTypeDomain>> getAllFuelTypes() {

        return new CustomResponse<List<FuelTypeDomain>>(
            carService.getAllFuelTypes(),
            HttpStatus.OK,
            "Fuel types retrieved successfully"
        );

    }

    @GetMapping("/transmission-type")
    public CustomResponse<List<transmissionTypeDomain>> getAllTransmissionTypes() {

        return new CustomResponse<List<transmissionTypeDomain>>(
            carService.getAllTransmissionTypes(),
            HttpStatus.OK,
            "Transmission types retrieved successfully"
        );

    }

    @PostMapping("/register")
    public CustomResponse<vehicleDomain> registerVehicle(
            @Valid @RequestBody carRegisterRequestDTO carRegisterRequestDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
            carService.registerVehicle(carRegisterRequestDTO, authenticatedUser.id()),
            HttpStatus.CREATED,
            "Vehicle registered successfully"
        );
    }

    @PostMapping("/fuel-logs")
    public CustomResponse<FuelLogResponseDTO> registerFuelLog(
            @Valid @RequestBody RegisterFuelLogDTO registerFuelLogDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                fuelService.registerFuelLog(registerFuelLogDTO, authenticatedUser.id()),
                HttpStatus.CREATED,
                "Fuel log registered successfully"
        );
    }

    @GetMapping("/fuel-logs")
    public CustomResponse<List<FuelLogResponseDTO>> getFuelLogs(
            @RequestParam Long vehicleId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                fuelService.getFuelLogs(vehicleId, authenticatedUser.id()),
                HttpStatus.OK,
                "Fuel logs retrieved successfully"
        );
    }

    @PutMapping("/fuel-logs/{fuelLogId}")
    public CustomResponse<FuelLogResponseDTO> updateFuelLog(
            @PathVariable Long fuelLogId,
            @Valid @RequestBody UpdateFuelLogDTO updateFuelLogDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                fuelService.updateFuelLog(fuelLogId, updateFuelLogDTO, authenticatedUser.id()),
                HttpStatus.OK,
                "Fuel log updated successfully"
        );
    }

    @DeleteMapping("/fuel-logs/{fuelLogId}")
    public CustomResponse<String> deleteFuelLog(
            @PathVariable Long fuelLogId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                fuelService.deleteFuelLog(fuelLogId, authenticatedUser.id()),
                HttpStatus.OK,
                "Fuel log deleted successfully"
        );
    }

    @GetMapping("/mileage-stats/monthly")
    public CustomResponse<List<MonthlyMileageStatsResponseDTO>> getMonthlyMileageStats(
            @RequestParam Long vehicleId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                odometerMileageStatsService.getMonthlyMileageStats(vehicleId, authenticatedUser.id()),
                HttpStatus.OK,
                "Monthly mileage stats retrieved successfully"
        );
    }

    @GetMapping("/mileage-stats/current-month")
    public CustomResponse<CurrentMonthMileageStatsResponseDTO> getCurrentMonthMileageStats(
            @RequestParam Long vehicleId,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                odometerMileageStatsService.getCurrentMonthMileageStats(vehicleId, authenticatedUser.id()),
                HttpStatus.OK,
                "Current month mileage stats retrieved successfully"
        );
    }

}
