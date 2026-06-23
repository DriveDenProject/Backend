package com.driveden.app.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.driveden.app.client.MotorcycleClient;
import com.driveden.app.domain.motorcycles.dto.MotorcycleMakeDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleModelDTO;
import com.driveden.app.domain.motorcycles.dto.MotorcycleProductionYearDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MotorcycleService {

    private final MotorcycleClient motorcycleClient;

    public List<MotorcycleMakeDTO> getAllMakes() {
        return motorcycleClient.getAllMakes();
    }

    public List<MotorcycleModelDTO> getModelsByMake(Long makeId) {
        return motorcycleClient.getModelsByMake(makeId);
    }

    public List<MotorcycleProductionYearDTO> getProductionYears(Long modelId) {
        return motorcycleClient.getProductionYears(modelId);
    }

    public Long getArticleId(Integer year, String make, String model) {
        return motorcycleClient.getArticleId(year, make, model);
    }

    public String getMotorcycleImageLink(Long articleId) {
        return motorcycleClient.getMotorcycleImageLink(articleId);
    }
}
