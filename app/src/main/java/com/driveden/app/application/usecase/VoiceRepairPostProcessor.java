package com.driveden.app.application.usecase;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.driveden.app.domain.voice.model.RepairCostCandidate;
import com.driveden.app.domain.voice.model.VoiceClassificationResult;
import com.driveden.app.domain.voice.model.VoiceClassificationType;

@Component
public class VoiceRepairPostProcessor {

    private final RepairCostExtractor repairCostExtractor;

    public VoiceRepairPostProcessor(RepairCostExtractor repairCostExtractor) {
        this.repairCostExtractor = repairCostExtractor;
    }

    public VoiceClassificationResult process(String originalText, VoiceClassificationResult result) {
        if (result.type() != VoiceClassificationType.REPAIR || result.data() == null) {
            return result;
        }

        List<RepairCostCandidate> candidates = repairCostExtractor.extract(originalText);
        if (candidates.isEmpty()) {
            return sanitizeHallucinatedTextFields(originalText, result);
        }

        Map<String, Object> data = new LinkedHashMap<>(result.data());
        List<Map<String, Object>> parts = mutableParts(data.get("parts"));
        applyCandidatePrices(parts, candidates);
        addMissingCandidateParts(parts, candidates);

        data.put("parts", parts);
        data.put("workshop", keepOnlyIfPresent(originalText, data.get("workshop")));
        removeHallucinatedBrands(originalText, parts);
        data.put("totalCost", computeTotalCost(parts, data.get("laborCost")));

        return new VoiceClassificationResult(result.type(), data, result.message(), result.language());
    }

    private VoiceClassificationResult sanitizeHallucinatedTextFields(String originalText, VoiceClassificationResult result) {
        Map<String, Object> data = new LinkedHashMap<>(result.data());
        List<Map<String, Object>> parts = mutableParts(data.get("parts"));
        data.put("workshop", keepOnlyIfPresent(originalText, data.get("workshop")));
        removeHallucinatedBrands(originalText, parts);
        data.put("parts", parts);
        return new VoiceClassificationResult(result.type(), data, result.message(), result.language());
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> mutableParts(Object rawParts) {
        if (!(rawParts instanceof List<?> rawList)) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> parts = new ArrayList<>();
        for (Object rawPart : rawList) {
            if (rawPart instanceof Map<?, ?> rawMap) {
                parts.add(new LinkedHashMap<>((Map<String, Object>) rawMap));
            }
        }
        return parts;
    }

    private void applyCandidatePrices(List<Map<String, Object>> parts, List<RepairCostCandidate> candidates) {
        for (RepairCostCandidate candidate : candidates) {
            Map<String, Object> matchingPart = findMatchingPart(parts, candidate);
            if (matchingPart != null && matchingPart.get("unitPrice") == null) {
                matchingPart.put("unitPrice", candidate.amount());
            }
        }
    }

    private void addMissingCandidateParts(List<Map<String, Object>> parts, List<RepairCostCandidate> candidates) {
        for (RepairCostCandidate candidate : candidates) {
            if (findMatchingPart(parts, candidate) != null) {
                continue;
            }

            Map<String, Object> part = new LinkedHashMap<>();
            part.put("name", candidate.phrase());
            part.put("categoryId", null);
            part.put("brand", null);
            part.put("quantity", null);
            part.put("unitPrice", candidate.amount());
            parts.add(part);
        }
    }

    private Map<String, Object> findMatchingPart(List<Map<String, Object>> parts, RepairCostCandidate candidate) {
        String normalizedPhrase = normalize(candidate.phrase());
        return parts.stream()
                .filter(part -> part.get("name") != null)
                .filter(part -> namesMatch(normalize(String.valueOf(part.get("name"))), normalizedPhrase))
                .findFirst()
                .orElse(null);
    }

    private boolean namesMatch(String partName, String phrase) {
        return partName.equals(phrase) || partName.contains(phrase) || phrase.contains(partName);
    }

    private BigDecimal computeTotalCost(List<Map<String, Object>> parts, Object laborCostValue) {
        BigDecimal total = BigDecimal.ZERO;

        for (Map<String, Object> part : parts) {
            BigDecimal unitPrice = asBigDecimal(part.get("unitPrice"));
            BigDecimal quantity = asBigDecimal(part.get("quantity"));
            if (unitPrice != null) {
                total = total.add(unitPrice.multiply(quantity == null ? BigDecimal.ONE : quantity));
            }
        }

        BigDecimal laborCost = asBigDecimal(laborCostValue);
        if (laborCost != null) {
            total = total.add(laborCost);
        }

        return total;
    }

    private void removeHallucinatedBrands(String originalText, List<Map<String, Object>> parts) {
        for (Map<String, Object> part : parts) {
            Object brand = part.get("brand");
            if (brand != null && keepOnlyIfPresent(originalText, brand) == null) {
                part.put("brand", null);
            }
        }
    }

    private Object keepOnlyIfPresent(String originalText, Object value) {
        if (value == null) {
            return null;
        }

        String textValue = String.valueOf(value).trim();
        if (textValue.isBlank()) {
            return null;
        }

        return normalize(originalText).contains(normalize(textValue)) ? value : null;
    }

    private BigDecimal asBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        try {
            return new BigDecimal(Objects.toString(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String normalize(String value) {
        String lower = value.toLowerCase(Locale.ROOT);
        String decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}", "").replaceAll("[^\\p{L}\\p{N}\\s]", " ").replaceAll("\\s+", " ").trim();
    }
}
