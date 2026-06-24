package com.driveden.app.infrastructure.ai;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.driveden.app.domain.voice.model.VoiceLanguage;

@Component
public class VoiceInputPrefilter {

    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{N}]+");
    private static final Pattern REPEATED_CHAR_SPAM = Pattern.compile("(.)\\1{8,}");

    private static final Set<String> VEHICLE_KEYWORDS = Set.of(
            "fuel", "gas", "gallon", "gallons", "odometer", "mileage", "filled", "fill", "station",
            "oil", "change", "brake", "brakes", "tire", "tires", "repair", "workshop", "service",
            "maintenance", "reminder", "battery", "filter", "coolant", "engine", "mechanic",
            "gasolina", "galon", "galones", "kilometraje", "odometro", "tanque", "llenar", "llene",
            "estacion", "aceite", "cambio", "freno", "frenos", "llanta", "llantas", "reparacion",
            "taller", "servicio", "mantenimiento", "recordatorio", "bateria", "filtro", "motor",
            "mecanico", "pastillas", "correa", "bujia", "bujias"
    );

    private static final Set<String> SPANISH_MARKERS = Set.of(
            "hola", "como", "estas", "el", "la", "los", "las", "un", "una", "de", "para", "cada", "meses", "hoy",
            "ayer", "pesos", "aceite", "cambio", "frenos", "taller", "recordatorio", "gasolina"
    );

    private static final Set<String> ENGLISH_MARKERS = Set.of(
            "the", "a", "an", "for", "every", "today", "yesterday", "dollars", "oil", "change",
            "brakes", "workshop", "reminder", "fuel", "gas"
    );

    public boolean shouldReject(String text) {
        if (text == null || text.isBlank()) {
            return true;
        }

        String normalized = normalize(text);
        if (REPEATED_CHAR_SPAM.matcher(normalized).find()) {
            return true;
        }

        String[] words = words(normalized);
        if (words.length < 3) {
            return true;
        }

        return Arrays.stream(words).noneMatch(VEHICLE_KEYWORDS::contains);
    }

    public VoiceLanguage language(String text) {
        return detectLanguage(text);
    }

    public static VoiceLanguage detectLanguage(String text) {
        String normalized = normalize(text);
        String[] words = words(normalized);

        long spanishScore = Arrays.stream(words).filter(SPANISH_MARKERS::contains).count();
        long englishScore = Arrays.stream(words).filter(ENGLISH_MARKERS::contains).count();

        return spanishScore > englishScore ? VoiceLanguage.SPANISH : VoiceLanguage.ENGLISH;
    }

    private static String normalize(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        String decomposed = Normalizer.normalize(lower, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}", "");
    }

    private static String[] words(String normalizedText) {
        return WORD_PATTERN.matcher(normalizedText)
                .results()
                .map(match -> match.group().toLowerCase(Locale.ROOT))
                .toArray(String[]::new);
    }
}
