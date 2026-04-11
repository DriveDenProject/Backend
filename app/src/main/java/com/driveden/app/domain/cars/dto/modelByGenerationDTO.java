package com.driveden.app.domain.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class modelByGenerationDTO {
    private String id;
    private String name;
    private Integer yearFrom;
    private Integer yearTo; 
}
