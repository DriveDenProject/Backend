package com.driveden.app.application.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.PartCategoryRepositoryPort;
import com.driveden.app.application.ports.out.PartRepositoryPort;
import com.driveden.app.application.ports.out.RepairPartRepositoryPort;
import com.driveden.app.application.ports.out.RepairRepositoryPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.common.dto.PageResponseDTO;
import com.driveden.app.domain.repairs.dto.LatestRepairByCategoryResponseDTO;
import com.driveden.app.domain.repairs.dto.RegisterRepairDTO;
import com.driveden.app.domain.repairs.dto.RegisterRepairPartDTO;
import com.driveden.app.domain.repairs.dto.RepairHistoryResponseDTO;
import com.driveden.app.domain.repairs.dto.RepairPartResponseDTO;
import com.driveden.app.domain.repairs.dto.RepairResponseDTO;
import com.driveden.app.domain.repairs.dto.RepairStatsResponseDTO;
import com.driveden.app.domain.repairs.model.PartDomain;
import com.driveden.app.domain.repairs.model.RepairDomain;
import com.driveden.app.domain.repairs.model.RepairHistoryDomain;
import com.driveden.app.domain.repairs.model.RepairPartDomain;
import com.driveden.app.infrastructure.out.persistence.mappers.RepairMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RepairService {

    private static final int MAX_PAGE_SIZE = 50;

    private final RepairRepositoryPort repairRepository;
    private final PartRepositoryPort partRepository;
    private final RepairPartRepositoryPort repairPartRepository;
    private final PartCategoryRepositoryPort partCategoryRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final UsersService usersService;

    @Transactional
    public RepairResponseDTO registerRepair(RegisterRepairDTO registerRepairDTO, Long userId) {
        validateVehicleOwnership(userId, registerRepairDTO.getVehicleId());
        validateDuplicatedParts(registerRepairDTO.getParts());
        validateCategoriesExist(registerRepairDTO.getParts());

        Map<String, PartDomain> resolvedParts = resolveParts(registerRepairDTO.getParts());
        RepairDomain savedRepair = createRepair(registerRepairDTO);
        List<RepairPartDomain> savedRepairParts = saveRepairParts(savedRepair.getId(), registerRepairDTO.getParts(), resolvedParts);

        return buildResponse(savedRepair, registerRepairDTO.getParts(), resolvedParts, savedRepairParts);
    }

    public PageResponseDTO<RepairHistoryResponseDTO> getRepairHistory(Long vehicleId, Pageable pageable, Long userId) {
        validateVehicleOwnership(userId, vehicleId);
        validatePagination(pageable);

        Page<RepairHistoryDomain> history = repairRepository.findHistoryByVehicleId(vehicleId, pageable);

        return PageResponseDTO.from(
                history.map(RepairMapper::toHistoryResponseDTO)
        );
    }

    public RepairStatsResponseDTO getRepairStats(Long vehicleId, Long userId) {
        validateVehicleOwnership(userId, vehicleId);

        return RepairMapper.toStatsResponseDTO(
                repairRepository.findStatsByVehicleId(vehicleId)
        );
    }

    public List<LatestRepairByCategoryResponseDTO> getLatestRepairsByCategory(
            Long vehicleId,
            Long categoryId,
            Long userId
    ) {
        validateVehicleOwnership(userId, vehicleId);
        validateCategoryExists(categoryId);

        return repairRepository.findLatestByVehicleIdAndCategoryId(vehicleId, categoryId).stream()
                .map(RepairMapper::toLatestRepairByCategoryResponseDTO)
                .toList();
    }

    private void validateVehicleOwnership(Long userId, Long vehicleId) {
        usersService.findUserById(userId);

        if (!userVehicleRepository.existsByUserIdAndVehicleId(userId, vehicleId)) {
            throw new CustomException("Vehicle not found for user", HttpStatus.NOT_FOUND);
        }
    }

    private void validateDuplicatedParts(List<RegisterRepairPartDTO> parts) {
        Set<String> duplicatedPartNames = parts.stream()
                .map(part -> normalizePartName(part.getName()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (!duplicatedPartNames.isEmpty()) {
            throw new CustomException("Duplicated parts are not allowed: " + duplicatedPartNames, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateCategoriesExist(List<RegisterRepairPartDTO> parts) {
        Set<Long> requestedCategoryIds = parts.stream()
                .map(RegisterRepairPartDTO::getCategoryId)
                .collect(Collectors.toSet());
        Set<Long> existingCategoryIds = partCategoryRepository.findExistingIds(requestedCategoryIds);

        if (existingCategoryIds.size() != requestedCategoryIds.size()) {
            requestedCategoryIds.removeAll(existingCategoryIds);
            throw new CustomException("Part categories not found: " + requestedCategoryIds, HttpStatus.NOT_FOUND);
        }
    }

    private void validateCategoryExists(Long categoryId) {
        if (!partCategoryRepository.existsById(categoryId)) {
            throw new CustomException("Part category not found", HttpStatus.NOT_FOUND);
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

    private Map<String, PartDomain> resolveParts(List<RegisterRepairPartDTO> requestedParts) {
        List<String> normalizedNames = requestedParts.stream()
                .map(part -> normalizePartName(part.getName()))
                .toList();
        Map<String, PartDomain> existingParts = findExistingParts(normalizedNames);
        List<PartDomain> missingParts = buildMissingParts(requestedParts, existingParts);
        List<PartDomain> createdParts = createMissingParts(missingParts);

        Map<String, PartDomain> resolvedParts = new LinkedHashMap<>(existingParts);
        createdParts.forEach(part -> resolvedParts.put(normalizePartName(part.getName()), part));

        return resolvedParts;
    }

    private Map<String, PartDomain> findExistingParts(List<String> normalizedNames) {
        return partRepository.findByNamesIgnoreCase(normalizedNames).stream()
                .collect(Collectors.toMap(
                        part -> normalizePartName(part.getName()),
                        Function.identity(),
                        (first, ignored) -> first,
                        LinkedHashMap::new
                ));
    }

    private List<PartDomain> buildMissingParts(
            List<RegisterRepairPartDTO> requestedParts,
            Map<String, PartDomain> existingParts
    ) {
        return requestedParts.stream()
                .filter(part -> !existingParts.containsKey(normalizePartName(part.getName())))
                .map(part -> new PartDomain(
                        null,
                        part.getCategoryId(),
                        part.getName().trim(),
                        normalizeNullableText(part.getBrand())
                ))
                .toList();
    }

    private List<PartDomain> createMissingParts(List<PartDomain> missingParts) {
        if (missingParts.isEmpty()) {
            return List.of();
        }

        return partRepository.saveAll(missingParts);
    }

    private RepairDomain createRepair(RegisterRepairDTO registerRepairDTO) {
        return repairRepository.save(
                RepairMapper.fromDTOtoDomain(registerRepairDTO)
        );
    }

    private List<RepairPartDomain> saveRepairParts(
            Long repairId,
            List<RegisterRepairPartDTO> requestedParts,
            Map<String, PartDomain> resolvedParts
    ) {
        List<RepairPartDomain> repairParts = buildRepairParts(repairId, requestedParts, resolvedParts);
        return repairPartRepository.saveAll(repairParts);
    }

    private List<RepairPartDomain> buildRepairParts(
            Long repairId,
            List<RegisterRepairPartDTO> requestedParts,
            Map<String, PartDomain> resolvedParts
    ) {
        List<RepairPartDomain> repairParts = new ArrayList<>();

        for (RegisterRepairPartDTO requestedPart : requestedParts) {
            PartDomain part = resolvedParts.get(normalizePartName(requestedPart.getName()));
            repairParts.add(new RepairPartDomain(
                    repairId,
                    part.getId(),
                    requestedPart.getQuantity(),
                    requestedPart.getUnitPrice(),
                    requestedPart.getWarrantyExpiration(),
                    requestedPart.getPartExpiration()
            ));
        }

        return repairParts;
    }

    private RepairResponseDTO buildResponse(
            RepairDomain repair,
            List<RegisterRepairPartDTO> requestedParts,
            Map<String, PartDomain> resolvedParts,
            List<RepairPartDomain> savedRepairParts
    ) {
        Map<Long, RepairPartDomain> repairPartByPartId = savedRepairParts.stream()
                .collect(Collectors.toMap(RepairPartDomain::getPartId, Function.identity()));

        List<RepairPartResponseDTO> partsResponse = requestedParts.stream()
                .map(requestedPart -> buildPartResponse(requestedPart, resolvedParts, repairPartByPartId))
                .toList();

        return new RepairResponseDTO(
                repair.getId(),
                repair.getVehicleId(),
                repair.getRepairDate(),
                repair.getDescription(),
                repair.getWorkshop(),
                repair.getLaborCost(),
                repair.getTotalCost(),
                savedRepairParts.size(),
                partsResponse
        );
    }

    private RepairPartResponseDTO buildPartResponse(
            RegisterRepairPartDTO requestedPart,
            Map<String, PartDomain> resolvedParts,
            Map<Long, RepairPartDomain> repairPartByPartId
    ) {
        PartDomain part = resolvedParts.get(normalizePartName(requestedPart.getName()));
        RepairPartDomain repairPart = repairPartByPartId.get(part.getId());

        return new RepairPartResponseDTO(
                part.getId(),
                part.getName(),
                part.getCategoryId(),
                repairPart.getQuantity(),
                repairPart.getCost(),
                repairPart.getWarrantyExpiration(),
                repairPart.getPartExpiration()
        );
    }

    private String normalizePartName(String name) {
        return name.trim().toLowerCase();
    }

    private String normalizeNullableText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
