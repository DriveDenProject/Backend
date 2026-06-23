package com.driveden.app.infrastructure.controllers.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.MotorcycleService;
import com.driveden.app.domain.motorcycles.dto.MotorcycleMakeDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleModelDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleProductionYearDTO;
import com.driveden.app.utils.CustomResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/motorcycles")
@Validated
public class MotorcyclesController {

    private final MotorcycleService motorcycleService;

    @GetMapping("/makes")
    public CustomResponse<List<MotorcycleMakeDTO>> getAllMakes() {
        return new CustomResponse<>(
                motorcycleService.getAllMakes(),
                HttpStatus.OK,
                "Motorcycle makes retrieved successfully"
        );
    }

    @GetMapping("/models")
    public CustomResponse<List<MotorcycleModelDTO>> getModelsByMake(@RequestParam Long makeId) {
        return new CustomResponse<>(
                motorcycleService.getModelsByMake(makeId),
                HttpStatus.OK,
                "Motorcycle models retrieved successfully"
        );
    }

    @GetMapping("/models/{modelId}/years")
    public CustomResponse<List<MotorcycleProductionYearDTO>> getProductionYears(@PathVariable Long modelId) {
        return new CustomResponse<>(
                motorcycleService.getProductionYears(modelId),
                HttpStatus.OK,
                "Motorcycle production years retrieved successfully"
        );
    }

    @GetMapping("/articles/id")
    public CustomResponse<Long> getArticleId(
            @RequestParam Integer year,
            @RequestParam String make,
            @RequestParam String model
    ) {
        return new CustomResponse<>(
                motorcycleService.getArticleId(year, make, model),
                HttpStatus.OK,
                "Motorcycle article ID retrieved successfully"
        );
    }

    @GetMapping("/articles/{articleId}/image-link")
    public CustomResponse<String> getMotorcycleImageLink(@PathVariable Long articleId) {
        return new CustomResponse<>(
                motorcycleService.getMotorcycleImageLink(articleId),
                HttpStatus.OK,
                "Motorcycle image link retrieved successfully"
        );
    }
}
