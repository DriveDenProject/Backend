package com.driveden.app.domain.repairs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartDomain {

    private Long id;
    private Long categoryId;
    private String name;
    private String brand;
}
