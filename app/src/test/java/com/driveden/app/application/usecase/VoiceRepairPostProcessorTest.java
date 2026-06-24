package com.driveden.app.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceClassificationType;
import com.driveden.app.domain.voice.model.VoiceLanguage;

class VoiceRepairPostProcessorTest {

    private final VoiceRepairPostProcessor postProcessor = new VoiceRepairPostProcessor(new RepairCostExtractor());

    @Test
    @SuppressWarnings("unchecked")
    void mapsExplicitItemPricesAndCorrectsWrongTotalCost() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("repairDate", "2026-06-24");
        data.put("description", "Cambio de llantas, frenos traseros y luces");
        data.put("workshop", null);
        data.put("laborCost", null);
        data.put("totalCost", new BigDecimal("100000"));
        data.put("parts", List.of(
                part("llantas", null, "Suzuki"),
                part("frenos traseros", null, null),
                part("luces", null, null)
        ));

        VoiceClassificationResult processed = postProcessor.process(
                "le cambiaron las llantas por 100 mil pesos, le cambiaron los frenos traseros por 60 mil pesos, le arreglaron un fallo en las luces por 40 mil pesos",
                new VoiceClassificationResult(VoiceClassificationType.REPAIR, data, null, VoiceLanguage.SPANISH)
        );

        List<Map<String, Object>> parts = (List<Map<String, Object>>) processed.data().get("parts");
        assertThat(parts.get(0)).containsEntry("unitPrice", new BigDecimal("100000"));
        assertThat(parts.get(1)).containsEntry("unitPrice", new BigDecimal("60000"));
        assertThat(parts.get(2)).containsEntry("unitPrice", new BigDecimal("40000"));
        assertThat(parts.get(0)).containsEntry("brand", null);
        assertThat(processed.data()).containsEntry("totalCost", new BigDecimal("200000"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void addsServiceOnlyRepairItemWhenAiOmittedIt() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("repairDate", "2026-06-24");
        data.put("description", "Arreglo de luces");
        data.put("workshop", "Taller inventado");
        data.put("laborCost", new BigDecimal("20000"));
        data.put("totalCost", null);
        data.put("parts", List.of());

        VoiceClassificationResult processed = postProcessor.process(
                "arreglaron un fallo en las luces por 40 mil pesos",
                new VoiceClassificationResult(VoiceClassificationType.REPAIR, data, null, VoiceLanguage.SPANISH)
        );

        List<Map<String, Object>> parts = (List<Map<String, Object>>) processed.data().get("parts");
        assertThat(parts).hasSize(1);
        assertThat(parts.getFirst()).containsEntry("name", "fallo en las luces");
        assertThat(parts.getFirst()).containsEntry("unitPrice", new BigDecimal("40000"));
        assertThat(processed.data()).containsEntry("workshop", null);
        assertThat(processed.data()).containsEntry("totalCost", new BigDecimal("60000"));
    }

    private Map<String, Object> part(String name, BigDecimal unitPrice, String brand) {
        Map<String, Object> part = new LinkedHashMap<>();
        part.put("name", name);
        part.put("categoryId", null);
        part.put("brand", brand);
        part.put("quantity", null);
        part.put("unitPrice", unitPrice);
        return part;
    }
}
