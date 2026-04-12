package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.CarService;
import com.driveden.app.domain.cars.dto.carRegisterRequestDTO;
import com.driveden.app.domain.cars.dto.makesDTO;
import com.driveden.app.domain.cars.dto.modelByGenerationDTO;
import com.driveden.app.domain.cars.dto.modelsDTO;
import com.driveden.app.domain.cars.model.vehicleDomain;
import com.driveden.app.domain.fuelType.model.FuelTypeDomain;
import com.driveden.app.domain.transmissionType.model.transmissionTypeDomain;
import com.driveden.app.utils.CustomResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarsController {

    private final CarService carService;

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
    public CustomResponse<vehicleDomain> registerVehicle(@RequestBody carRegisterRequestDTO carRegisterRequestDTO) {

        return new CustomResponse<>(
            carService.registerVehicle(carRegisterRequestDTO),
            HttpStatus.CREATED,
            "Vehicle registered successfully"
        );
        
    }

}