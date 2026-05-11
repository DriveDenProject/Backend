package com.driveden.app.application.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.client.CarSpecsClient;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.cars.dto.carRegisterRequestDTO;
import com.driveden.app.domain.cars.dto.makesDTO;
import com.driveden.app.domain.cars.dto.modelByGenerationDTO;
import com.driveden.app.domain.cars.dto.modelsDTO;
import com.driveden.app.domain.cars.model.vehicleDetailsDomain;
import com.driveden.app.domain.cars.model.vehicleDomain;
import com.driveden.app.domain.fuelType.model.FuelTypeDomain;
import com.driveden.app.domain.transmissionType.model.transmissionTypeDomain;
import com.driveden.app.domain.users.model.UserVehicleDomain;
import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.entity.UserVehicleEntity;
import com.driveden.app.infrastructure.out.persistence.entity.UsersEntity;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleDetailsEntity;
import com.driveden.app.infrastructure.out.persistence.entity.VehicleEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.UserVehicleMapper;
import com.driveden.app.infrastructure.out.persistence.mappers.UsersMapper;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleDetailsMapper;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.FuelTypeRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.TransmissionTypeRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;
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
    private final UserVehicleRepository userVehicleRepository;
    private final UsersService usersService;

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
    public vehicleDomain registerVehicle(
            carRegisterRequestDTO carRegisterRequestDTO,
            Long userId
    ) {

        // Obtener User
        Users user = usersService.findUserById(userId);
        if(user == null){
            throw new CustomException("User not Found", HttpStatus.NOT_FOUND);
        }

        // 1. Guardar vehículo
        vehicleDomain vehicleDomain = VehicleMapper.fromDTOtoDomain(carRegisterRequestDTO);
        vehicleDomain savedVehicle = vehicleRepository.save(vehicleDomain);

        // 2. Guardar detalles
        vehicleDetailsDomain savedVehicleDetails =
                VehicleDetailsMapper.fromDTOtoDomain(carRegisterRequestDTO, savedVehicle.getId());

        VehicleDetailsEntity detailsEntity =
                VehicleDetailsMapper.toEntity(savedVehicleDetails);

        vehicleDetailsRepository.save(detailsEntity);

        UserVehicleDomain userHasMainVehicle = userVehicleRepository.findByIdUserIdAndIsPrimaryTrue(userId);

        // 3. RELACIONAR CON EL USUARIO
        UserVehicleDomain userVehicleDomain = UserVehicleDomain.builder()
                .userId(userId)
                .vehicleId(savedVehicle.getId())
                .build();

        if(userHasMainVehicle != null) {
            userVehicleDomain.setIsPrimary(false);
        }else{
            userVehicleDomain.setIsPrimary(true);
        }
        


        //Obtener UserEntity 
        UsersEntity usersEntity = UsersMapper.domaintoEntity(user);

        //Obtener VehicleEntity
        VehicleEntity vehicleEntity = VehicleMapper.toEntity(savedVehicle);

        //Mapper UserVehicleDomain a UserVehicleEntity
        UserVehicleEntity userVehicleEntity =
                UserVehicleMapper.toEntity(userVehicleDomain, usersEntity, vehicleEntity);

        userVehicleRepository.save(userVehicleEntity);

        // 4. retornar
        return savedVehicle;
    }

}
