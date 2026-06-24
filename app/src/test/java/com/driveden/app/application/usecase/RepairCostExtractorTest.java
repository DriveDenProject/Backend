package com.driveden.app.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class RepairCostExtractorTest {

    private final RepairCostExtractor extractor = new RepairCostExtractor();

    @Test
    void extractsMultipleRepairItemsWithPrices() {
        var candidates = extractor.extract(
                "Hoy arreglaron la moto, le cambiaron las llantas por 100 mil pesos, "
                        + "le cambiaron los frenos traseros por 60 mil pesos, "
                        + "le arreglaron un fallo en las luces por 40 mil pesos"
        );

        assertThat(candidates).hasSize(3);
        assertThat(candidates.get(0).phrase()).isEqualTo("llantas");
        assertThat(candidates.get(0).amount()).isEqualByComparingTo(new BigDecimal("100000"));
        assertThat(candidates.get(1).phrase()).isEqualTo("frenos traseros");
        assertThat(candidates.get(1).amount()).isEqualByComparingTo(new BigDecimal("60000"));
        assertThat(candidates.get(2).phrase()).isEqualTo("fallo en las luces");
        assertThat(candidates.get(2).amount()).isEqualByComparingTo(new BigDecimal("40000"));
    }

    @Test
    void normalizesCommonColombianPriceFormats() {
        assertThat(extractor.extract("llantas por 100 mil").getFirst().amount())
                .isEqualByComparingTo(new BigDecimal("100000"));
        assertThat(extractor.extract("llantas por 100k").getFirst().amount())
                .isEqualByComparingTo(new BigDecimal("100000"));
        assertThat(extractor.extract("llantas por 100.000").getFirst().amount())
                .isEqualByComparingTo(new BigDecimal("100000"));
        assertThat(extractor.extract("llantas por 100 lucas").getFirst().amount())
                .isEqualByComparingTo(new BigDecimal("100000"));
    }
}
