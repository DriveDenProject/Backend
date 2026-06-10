package com.driveden.app.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.PartCategoryRepositoryPort;
import com.driveden.app.domain.repairs.dto.PartCategoryResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartCategoryService {

    private final PartCategoryRepositoryPort partCategoryRepository;

    public List<PartCategoryResponseDTO> getPartCategories() {
        return partCategoryRepository.findAllOrderByNameAsc().stream()
                .map(category -> new PartCategoryResponseDTO(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }
}
