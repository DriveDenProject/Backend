package com.driveden.app.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.driveden.app.client.CarSpecsClient;
import com.driveden.app.domain.cars.dto.carRegisterRequestDTO;
import com.driveden.app.domain.cars.dto.makesDTO;
import com.driveden.app.domain.cars.dto.modelByGenerationDTO;
import com.driveden.app.domain.cars.dto.modelsDTO;
import com.driveden.app.domain.cars.model.vehicleDetailsDomain;
import com.driveden.app.domain.cars.model.vehicleDomain;
import com.driveden.app.domain.fuelType.model.FuelTypeDomain;
import com.driveden.app.domain.transmissionType.model.transmissionTypeDomain;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleDetailsEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleDetailsMapper;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.FuelTypeRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.TransmissionTypeRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleDetailsRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.VehicleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarSpecsClient carSpecsClient;
    private final FuelTypeRepository fuelTypeRepository;
    private final TransmissionTypeRepository transmissionTypeRepository;
    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final VehicleRepository vehicleRepository;

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

    public List<transmissionTypeDomain> getAllTransmissionTypes() {
        return transmissionTypeRepository.findAll();
    }

    @Transactional
    public vehicleDomain registerVehicle(carRegisterRequestDTO carRegisterRequestDTO) {

            //Mapper DTO to Domain
            vehicleDomain vehicleDomain = VehicleMapper.fromDTOtoDomain(carRegisterRequestDTO);
            //Save to DB
            vehicleDomain savedVehicle = vehicleRepository.save(vehicleDomain);
            //Maper to VehicleDetailsDomain
            vehicleDetailsDomain savedVehicleDetails = VehicleDetailsMapper.fromDTOtoDomain(carRegisterRequestDTO, savedVehicle.getId());
            //Save VehicleDetails to DB
            VehicleDetailsEntity entity = VehicleDetailsMapper.toEntity(savedVehicleDetails);
            vehicleDetailsRepository.save(entity);
            //Retornar el vehículo guardado con detalles
            return savedVehicle; 
    }

}
