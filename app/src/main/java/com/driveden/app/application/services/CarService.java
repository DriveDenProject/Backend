package com.driveden.app.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.driveden.app.client.CarSpecsClient;
import com.driveden.app.domain.cars.dto.makesDTO;
import com.driveden.app.domain.cars.dto.modelByGenerationDTO;
import com.driveden.app.domain.cars.dto.modelsDTO;
import com.driveden.app.domain.fuelType.model.FuelTypeDomain;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.FuelTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarSpecsClient carSpecsClient;
    private final FuelTypeRepository fuelTypeRepository;

    public String getCarSpecs(String model) {
        return carSpecsClient.getCarSpecs(model);
    }

    public List<makesDTO> getAllMakes() {
        return carSpecsClient.getAllMakes();
    }

    public List<modelsDTO> getModelsByMake(String makeId) {
        return carSpecsClient.getModelsByMake(makeId);
    }

    public List<modelByGenerationDTO> getModelsByGeneration(String modelId) {
        return carSpecsClient.getModelsByGeneration(modelId);
    }

    public List<FuelTypeDomain> getAllFuelTypes() {
        return fuelTypeRepository.findAll();
    }



}
