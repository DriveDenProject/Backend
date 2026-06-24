package com.driveden.app.infrastructure.ai;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.driveden.app.domain.vehicleNotifications.model.VehicleNotificationPriority;
import com.driveden.app.domain.voice.exception.VoiceClassificationException;
import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceClassificationType;
import com.driveden.app.domain.voice.model.VoiceLanguage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenAIResponseParser {

    private static final Set<String> ROOT_FIELDS = Set.of("type", "data", "message");
    private static final Set<String> FUEL_FIELDS = Set.of("gallons", "priceTotal", "pricePerGallon", "kmAtFill", "filledAt", "gasStation");
    private static final Set<String> REMINDER_FIELDS = Set.of(
            "categoryId", "serviceName", "description", "startDate", "dueDate", "reminderFrequencyDays",
            "notifyBeforeDays", "priority", "isRecurring", "recurrenceIntervalDays"
    );
    private static final Set<String> REPAIR_FIELDS = Set.of("repairDate", "description", "workshop", "laborCost", "totalCost", "parts");
    private static final Set<String> REPAIR_PART_FIELDS = Set.of("name", "categoryId", "brand", "quantity", "unitPrice");

    private final ObjectMapper objectMapper;

    public VoiceClassificationResult parse(String rawJson, VoiceLanguage language) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            validateObject(root, "root");
            validateAllowedFields(root, ROOT_FIELDS, "root");

            VoiceClassificationType type = parseType(root.path("type").asText(null));
            if (type == VoiceClassificationType.INVALID_AUDIO) {
                return VoiceClassificationResult.invalid(defaultInvalidMessage(language), language);
            }

            JsonNode data = root.get("data");
            validateObject(data, "data");

            Map<String, Object> parsedData = switch (type) {
                case FUEL_LOG -> parseFuelData(data);
                case REMINDER -> parseReminderData(data);
                case REPAIR -> parseRepairData(data);
                case INVALID_AUDIO -> null;
            };

            return new VoiceClassificationResult(type, parsedData, null, language);
        } catch (JsonProcessingException ex) {
            throw new VoiceClassificationException("AI response is not valid JSON", ex);
        }
    }

    private Map<String, Object> parseFuelData(JsonNode data) {
        validateAllowedFields(data, FUEL_FIELDS, "fuel data");
        Map<String, Object> result = new LinkedHashMap<>();
        BigDecimal gallons = decimalOrNull(data.get("gallons"), "gallons", true);
        BigDecimal priceTotal = decimalOrNull(data.get("priceTotal"), "priceTotal", true);
        BigDecimal pricePerGallon = decimalOrNull(data.get("pricePerGallon"), "pricePerGallon", true);

        if (pricePerGallon == null && gallons != null && priceTotal != null && gallons.compareTo(BigDecimal.ZERO) > 0) {
            pricePerGallon = priceTotal.divide(gallons, 2, RoundingMode.HALF_UP);
        }

        result.put("gallons", gallons);
        result.put("priceTotal", priceTotal);
        result.put("pricePerGallon", pricePerGallon);
        result.put("kmAtFill", integerOrNull(data.get("kmAtFill"), "kmAtFill", false));
        result.put("filledAt", localDateTimeOrNull(data.get("filledAt"), "filledAt"));
        result.put("gasStation", textOrNull(data.get("gasStation"), "gasStation", 100));
        return result;
    }

    private Map<String, Object> parseReminderData(JsonNode data) {
        validateAllowedFields(data, REMINDER_FIELDS, "reminder data");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("categoryId", longOrNull(data.get("categoryId"), "categoryId", true));
        result.put("serviceName", textOrNull(data.get("serviceName"), "serviceName", 150));
        result.put("description", textOrNull(data.get("description"), "description", 1000));
        result.put("startDate", localDateOrNull(data.get("startDate"), "startDate"));
        result.put("dueDate", localDateOrNull(data.get("dueDate"), "dueDate"));
        result.put("reminderFrequencyDays", integerOrNull(data.get("reminderFrequencyDays"), "reminderFrequencyDays", true));
        result.put("notifyBeforeDays", integerOrNull(data.get("notifyBeforeDays"), "notifyBeforeDays", false));
        result.put("priority", priorityOrNull(data.get("priority")));
        result.put("isRecurring", booleanOrNull(data.get("isRecurring"), "isRecurring"));
        result.put("recurrenceIntervalDays", integerOrNull(data.get("recurrenceIntervalDays"), "recurrenceIntervalDays", true));
        return result;
    }

    private Map<String, Object> parseRepairData(JsonNode data) {
        validateAllowedFields(data, REPAIR_FIELDS, "repair data");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("repairDate", localDateOrNull(data.get("repairDate"), "repairDate"));
        result.put("description", textOrNull(data.get("description"), "description", 1000));
        result.put("workshop", textOrNull(data.get("workshop"), "workshop", 150));
        result.put("laborCost", decimalOrNull(data.get("laborCost"), "laborCost", false));
        result.put("totalCost", decimalOrNull(data.get("totalCost"), "totalCost", false));
        result.put("parts", parseRepairParts(data.get("parts")));
        return result;
    }

    private List<Map<String, Object>> parseRepairParts(JsonNode parts) {
        if (parts == null || parts.isNull()) {
            return List.of();
        }
        if (!parts.isArray()) {
            throw new VoiceClassificationException("parts must be an array");
        }

        return objectMapper.convertValue(parts, new TypeReference<List<JsonNode>>() {}).stream()
                .map(this::parseRepairPart)
                .toList();
    }

    private Map<String, Object> parseRepairPart(JsonNode part) {
        validateObject(part, "repair part");
        validateAllowedFields(part, REPAIR_PART_FIELDS, "repair part");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", textOrNull(part.get("name"), "name", 200));
        result.put("categoryId", longOrNull(part.get("categoryId"), "categoryId", true));
        result.put("brand", textOrNull(part.get("brand"), "brand", 100));
        result.put("quantity", integerOrNull(part.get("quantity"), "quantity", true));
        result.put("unitPrice", decimalOrNull(part.get("unitPrice"), "unitPrice", true));
        return result;
    }

    private VoiceClassificationType parseType(String rawType) {
        try {
            return VoiceClassificationType.valueOf(rawType);
        } catch (Exception ex) {
            throw new VoiceClassificationException("AI response type is not allowed");
        }
    }

    private void validateObject(JsonNode node, String name) {
        if (node == null || node.isNull() || !node.isObject()) {
            throw new VoiceClassificationException(name + " must be an object");
        }
    }

    private void validateAllowedFields(JsonNode node, Set<String> allowedFields, String name) {
        Iterator<String> fields = node.fieldNames();
        while (fields.hasNext()) {
            String field = fields.next();
            if (!allowedFields.contains(field)) {
                throw new VoiceClassificationException("Unexpected field in " + name + ": " + field);
            }
        }
    }

    private BigDecimal decimalOrNull(JsonNode node, String field, boolean positiveOnly) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (!node.isNumber()) {
            throw new VoiceClassificationException(field + " must be numeric");
        }
        BigDecimal value = node.decimalValue();
        if (positiveOnly && value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new VoiceClassificationException(field + " must be positive");
        }
        if (!positiveOnly && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new VoiceClassificationException(field + " must be zero or positive");
        }
        return value;
    }

    private Long longOrNull(JsonNode node, String field, boolean positiveOnly) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (!node.canConvertToLong()) {
            throw new VoiceClassificationException(field + " must be an integer");
        }
        long value = node.asLong();
        if (positiveOnly && value <= 0) {
            throw new VoiceClassificationException(field + " must be positive");
        }
        if (!positiveOnly && value < 0) {
            throw new VoiceClassificationException(field + " must be zero or positive");
        }
        return value;
    }

    private Integer integerOrNull(JsonNode node, String field, boolean positiveOnly) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (!node.canConvertToInt()) {
            throw new VoiceClassificationException(field + " must be an integer");
        }
        int value = node.asInt();
        if (positiveOnly && value <= 0) {
            throw new VoiceClassificationException(field + " must be positive");
        }
        if (!positiveOnly && value < 0) {
            throw new VoiceClassificationException(field + " must be zero or positive");
        }
        return value;
    }

    private String textOrNull(JsonNode node, String field, int maxLength) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (!node.isTextual()) {
            throw new VoiceClassificationException(field + " must be text");
        }
        String value = node.asText().trim();
        if (value.isEmpty()) {
            return null;
        }
        if (value.length() > maxLength) {
            throw new VoiceClassificationException(field + " is too long");
        }
        return value;
    }

    private String localDateOrNull(JsonNode node, String field) {
        String value = textOrNull(node, field, 30);
        if (value == null) {
            return null;
        }
        try {
            LocalDate.parse(value);
            return value;
        } catch (DateTimeParseException ex) {
            throw new VoiceClassificationException(field + " must be ISO_LOCAL_DATE", ex);
        }
    }

    private String localDateTimeOrNull(JsonNode node, String field) {
        String value = textOrNull(node, field, 40);
        if (value == null) {
            return null;
        }
        try {
            LocalDateTime.parse(value);
            return value;
        } catch (DateTimeParseException ex) {
            throw new VoiceClassificationException(field + " must be ISO_LOCAL_DATE_TIME", ex);
        }
    }

    private Boolean booleanOrNull(JsonNode node, String field) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (!node.isBoolean()) {
            throw new VoiceClassificationException(field + " must be boolean");
        }
        return node.asBoolean();
    }

    private String priorityOrNull(JsonNode node) {
        String value = textOrNull(node, "priority", 10);
        if (value == null) {
            return null;
        }
        try {
            return VehicleNotificationPriority.valueOf(value).name();
        } catch (Exception ex) {
            throw new VoiceClassificationException("priority is not allowed");
        }
    }

    private String defaultInvalidMessage(VoiceLanguage language) {
        if (language == VoiceLanguage.SPANISH) {
            return "Audio no corresponde a registros vehiculares";
        }
        return "Audio not related to vehicle records";
    }
}
