package com.driveden.app.domain.motorcycles.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MotorcycleProductionYearDTO {
    private String makeName;
    private String modelName;
    private Integer year;
}
