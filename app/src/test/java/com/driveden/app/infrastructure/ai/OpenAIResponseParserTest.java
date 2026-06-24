package com.driveden.app.infrastructure.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.driveden.app.domain.voice.exception.VoiceClassificationException;
import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceClassificationType;
import com.driveden.app.domain.voice.model.VoiceLanguage;
import com.fasterxml.jackson.databind.ObjectMapper;

class OpenAIResponseParserTest {

    private final OpenAIResponseParser parser = new OpenAIResponseParser(new ObjectMapper());

    @Test
    void parsesValidFuelInputAndComputesPricePerGallon() {
        String response = """
                {
                  "type": "FUEL_LOG",
                  "data": {
                    "gallons": 10.5,
                    "priceTotal": 165000,
                    "pricePerGallon": null,
                    "kmAtFill": 50210,
                    "filledAt": "2026-05-24T13:30:00",
                    "gasStation": "Terpel"
                  }
                }
                """;

        VoiceClassificationResult result = parser.parse(response, VoiceLanguage.ENGLISH);

        assertThat(result.type()).isEqualTo(VoiceClassificationType.FUEL_LOG);
        assertThat(result.data()).containsEntry("pricePerGallon", new BigDecimal("15714.29"));
        assertThat(result.data()).containsEntry("filledAt", "2026-05-24T13:30:00");
    }

    @Test
    void parsesValidReminderInputAndPreservesSpanishDescription() {
        String response = """
                {
                  "type": "REMINDER",
                  "data": {
                    "categoryId": 1,
                    "serviceName": "Cambio de aceite",
                    "description": "Cambio de aceite cada 6 meses",
                    "startDate": "2026-06-01",
                    "dueDate": "2026-12-01",
                    "reminderFrequencyDays": 3,
                    "notifyBeforeDays": 7,
                    "priority": "MEDIUM",
                    "isRecurring": true,
                    "recurrenceIntervalDays": 180
                  }
                }
                """;

        VoiceClassificationResult result = parser.parse(response, VoiceLanguage.SPANISH);

        assertThat(result.type()).isEqualTo(VoiceClassificationType.REMINDER);
        assertThat(result.data()).containsEntry("description", "Cambio de aceite cada 6 meses");
        assertThat(result.data()).containsEntry("priority", "MEDIUM");
    }

    @Test
    @SuppressWarnings("unchecked")
    void parsesValidRepairInputWithParts() {
        String response = """
                {
                  "type": "REPAIR",
                  "data": {
                    "repairDate": "2026-06-09",
                    "description": "Front brake pads",
                    "workshop": "Taller XYZ",
                    "laborCost": 150000,
                    "totalCost": 320000,
                    "parts": [
                      {
                        "name": "Brake pads",
                        "categoryId": 3,
                        "brand": "Bosch",
                        "quantity": 1,
                        "unitPrice": 120000
                      }
                    ]
                  }
                }
                """;

        VoiceClassificationResult result = parser.parse(response, VoiceLanguage.ENGLISH);

        assertThat(result.type()).isEqualTo(VoiceClassificationType.REPAIR);
        List<Map<String, Object>> parts = (List<Map<String, Object>>) result.data().get("parts");
        assertThat(parts).hasSize(1);
        assertThat(parts.get(0)).containsEntry("name", "Brake pads");
    }

    @Test
    void rejectsMalformedAiResponse() {
        assertThatThrownBy(() -> parser.parse("{not json", VoiceLanguage.ENGLISH))
                .isInstanceOf(VoiceClassificationException.class)
                .hasMessageContaining("valid JSON");
    }
}
