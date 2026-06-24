package com.driveden.app.application.usecase;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.driveden.app.domain.voice.model.RepairCostCandidate;

@Component
public class RepairCostExtractor {

    private static final int MAX_PHRASE_LENGTH = 80;

    private static final Pattern ITEM_PRICE_PATTERN = Pattern.compile(
            "(?<phrase>[\\p{L}\\p{N}\\s,.;:-]{2,80}?)\\s+(?:por|en|for)\\s+(?<amount>\\d+(?:[.,]\\d{3})*|\\d+)(?:\\s*(?<suffix>mil|k|lucas?|pesos?|cop))?",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    private static final Pattern LEADING_NOISE = Pattern.compile(
            "^(?:y\\s+)?(?:le\\s+)?(?:la\\s+|lo\\s+|las\\s+|los\\s+)?(?:cambiaron|cambiaron\\s+las|cambie|cambio|arreglaron|arregle|arreglo|repararon|repare|instalaron|instale|pusieron|puse)\\s+",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );
    private static final Pattern LEADING_ARTICLE = Pattern.compile(
            "^(?:el|la|los|las|un|una|unos|unas)\\s+",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    public List<RepairCostCandidate> extract(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String normalized = normalizeText(text);
        Matcher matcher = ITEM_PRICE_PATTERN.matcher(normalized);
        List<RepairCostCandidate> candidates = new ArrayList<>();

        while (matcher.find()) {
            String phrase = cleanPhrase(matcher.group("phrase"));
            BigDecimal amount = normalizeAmount(matcher.group("amount"), matcher.group("suffix"));

            if (!phrase.isBlank() && amount != null) {
                candidates.add(new RepairCostCandidate(phrase, amount));
            }
        }

        return candidates;
    }

    private String cleanPhrase(String phrase) {
        String cleaned = phrase
                .replaceAll("[,.;:]+$", "")
                .trim();

        int lastSeparator = Math.max(
                Math.max(cleaned.lastIndexOf(','), cleaned.lastIndexOf(';')),
                cleaned.lastIndexOf('.')
        );
        if (lastSeparator >= 0 && lastSeparator + 1 < cleaned.length()) {
            cleaned = cleaned.substring(lastSeparator + 1).trim();
        }

        cleaned = LEADING_NOISE.matcher(cleaned).replaceFirst("").trim();
        cleaned = LEADING_ARTICLE.matcher(cleaned).replaceFirst("").trim();

        if (cleaned.length() > MAX_PHRASE_LENGTH) {
            cleaned = cleaned.substring(cleaned.length() - MAX_PHRASE_LENGTH).trim();
        }

        return cleaned;
    }

    private BigDecimal normalizeAmount(String rawAmount, String suffix) {
        if (rawAmount == null || rawAmount.isBlank()) {
            return null;
        }

        String digits = rawAmount.replace(".", "").replace(",", "");
        if (digits.isBlank()) {
            return null;
        }

        BigDecimal amount = new BigDecimal(digits);
        String normalizedSuffix = suffix == null ? "" : normalizeText(suffix).toLowerCase(Locale.ROOT);
        if (normalizedSuffix.equals("mil") || normalizedSuffix.equals("k")
                || normalizedSuffix.equals("luca") || normalizedSuffix.equals("lucas")) {
            amount = amount.multiply(BigDecimal.valueOf(1000));
        }

        return amount;
    }

    private String normalizeText(String text) {
        String decomposed = Normalizer.normalize(text, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}", "");
    }
}
