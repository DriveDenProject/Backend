package com.driveden.app.application.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.VehicleHistoryRepositoryPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.common.dto.PageResponseDTO;
import com.driveden.app.domain.vehicleHistory.dto.VehicleHistoryItemResponseDTO;
import com.driveden.app.domain.vehicleHistory.model.VehicleHistoryItemDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.VehicleHistoryMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleHistoryService {

    private static final int MAX_PAGE_SIZE = 50;

    private final VehicleHistoryRepositoryPort vehicleHistoryRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final UsersService usersService;

    public PageResponseDTO<VehicleHistoryItemResponseDTO> getVehicleHistory(
            Long vehicleId,
            Pageable pageable,
            Long userId
    ) {
        validateVehicleOwnership(userId, vehicleId);
        validatePagination(pageable);

        Page<VehicleHistoryItemDomain> history = vehicleHistoryRepository.findByVehicleId(vehicleId, pageable);

        return PageResponseDTO.from(
                history.map(VehicleHistoryMapper::toResponseDTO)
        );
    }

    private void validateVehicleOwnership(Long userId, Long vehicleId) {
        usersService.findUserById(userId);

        if (!userVehicleRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new CustomException("Vehicle not found for user", HttpStatus.NOT_FOUND);
        }
    }

    private void validatePagination(Pageable pageable) {
        if (pageable.getPageNumber() < 0) {
            throw new CustomException("page must be greater than or equal to 0", HttpStatus.BAD_REQUEST);
        }

        if (pageable.getPageSize() <= 0) {
            throw new CustomException("size must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new CustomException("size must be less than or equal to " + MAX_PAGE_SIZE, HttpStatus.BAD_REQUEST);
        }
    }
}
