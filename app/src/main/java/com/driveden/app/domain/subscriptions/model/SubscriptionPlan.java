package com.driveden.app.domain.subscriptions.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan {

    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal monthlyPrice;
    private BigDecimal yearlyPrice;
    private String currency;
    private Integer maxVehicles;
    private Integer maxMonthlyScanImgs;
    private Integer maxMonthlyAudios;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> featureCodes;

    public boolean hasFeature(String featureCode) {
        return featureCodes != null && featureCodes.contains(featureCode);
    }
}
