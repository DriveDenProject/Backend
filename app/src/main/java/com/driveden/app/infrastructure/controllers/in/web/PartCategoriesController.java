package com.driveden.app.infrastructure.controllers.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.PartCategoryService;
import com.driveden.app.domain.repairs.dto.PartCategoryResponseDTO;
import com.driveden.app.utils.CustomResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/part-categories")
@Validated
public class PartCategoriesController {

    private final PartCategoryService partCategoryService;

    @GetMapping
    public CustomResponse<List<PartCategoryResponseDTO>> getPartCategories() {
        return new CustomResponse<>(
                partCategoryService.getPartCategories(),
                HttpStatus.OK,
                "Part categories retrieved successfully"
        );
    }
}
