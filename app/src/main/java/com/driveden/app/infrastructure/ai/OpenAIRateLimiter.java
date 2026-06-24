package com.driveden.app.infrastructure.ai;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.driveden.app.common.exception.CustomException;

@Component
public class OpenAIRateLimiter {

    private static final int MAX_USER_REQUESTS_PER_MINUTE = 20;
    private static final int MAX_IP_REQUESTS_PER_MINUTE = 60;
    private static final int FAILURE_COOLDOWN_THRESHOLD = 3;
    private static final Duration WINDOW = Duration.ofMinutes(1);
    private static final Duration FAILURE_COOLDOWN = Duration.ofSeconds(30);

    private final Clock clock;
    private final Map<String, Deque<Instant>> userRequests = new ConcurrentHashMap<>();
    private final Map<String, Deque<Instant>> ipRequests = new ConcurrentHashMap<>();
    private final Map<Long, FailureState> failures = new ConcurrentHashMap<>();

    public OpenAIRateLimiter(Clock clock) {
        this.clock = clock;
    }

    public void check(Long userId, String ipAddress) {
        Instant now = Instant.now(clock);
        FailureState failureState = failures.get(userId);
        if (failureState != null && failureState.cooldownUntil != null && now.isBefore(failureState.cooldownUntil)) {
            throw new CustomException("Too many failed voice parsing attempts. Please try again later.", HttpStatus.TOO_MANY_REQUESTS);
        }

        checkBucket(userRequests.computeIfAbsent(String.valueOf(userId), ignored -> new ArrayDeque<>()), now, MAX_USER_REQUESTS_PER_MINUTE);
        checkBucket(ipRequests.computeIfAbsent(ipAddress, ignored -> new ArrayDeque<>()), now, MAX_IP_REQUESTS_PER_MINUTE);
    }

    public void recordSuccess(Long userId) {
        failures.remove(userId);
    }

    public void recordFailure(Long userId) {
        Instant now = Instant.now(clock);
        failures.compute(userId, (ignored, current) -> {
            FailureState state = current == null || Duration.between(current.lastFailure, now).compareTo(WINDOW) > 0
                    ? new FailureState(0, now, null)
                    : current;
            int count = state.count + 1;
            Instant cooldownUntil = count >= FAILURE_COOLDOWN_THRESHOLD ? now.plus(FAILURE_COOLDOWN) : null;
            return new FailureState(count, now, cooldownUntil);
        });
    }

    private void checkBucket(Deque<Instant> bucket, Instant now, int limit) {
        synchronized (bucket) {
            while (!bucket.isEmpty() && Duration.between(bucket.peekFirst(), now).compareTo(WINDOW) > 0) {
                bucket.removeFirst();
            }

            if (bucket.size() >= limit) {
                throw new CustomException("Voice parsing rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS);
            }

            bucket.addLast(now);
        }
    }

    private record FailureState(int count, Instant lastFailure, Instant cooldownUntil) {
    }
}
