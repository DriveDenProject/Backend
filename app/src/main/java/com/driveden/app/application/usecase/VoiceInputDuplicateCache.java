package com.driveden.app.application.usecase;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.driveden.app.domain.voice.model.VoiceClassificationResult;

@Component
public class VoiceInputDuplicateCache {

    private static final Duration TTL = Duration.ofMinutes(5);

    private final Clock clock;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public VoiceInputDuplicateCache(Clock clock) {
        this.clock = clock;
    }

    public Optional<VoiceClassificationResult> get(Long userId, String text) {
        CacheEntry entry = cache.get(key(userId, text));
        if (entry == null) {
            return Optional.empty();
        }

        if (Instant.now(clock).isAfter(entry.expiresAt())) {
            cache.remove(key(userId, text));
            return Optional.empty();
        }

        return Optional.of(entry.result());
    }

    public void put(Long userId, String text, VoiceClassificationResult result) {
        cache.put(key(userId, text), new CacheEntry(result, Instant.now(clock).plus(TTL)));
    }

    private String key(Long userId, String text) {
        return userId + ":" + text.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private record CacheEntry(VoiceClassificationResult result, Instant expiresAt) {
    }
}
