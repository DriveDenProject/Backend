package com.driveden.app.application.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.SubscriptionPlanRepositoryPort;
import com.driveden.app.application.ports.out.SubscriptionUsageRepositoryPort;
import com.driveden.app.application.ports.out.UserSubscriptionRepositoryPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.subscriptions.dto.ActivateSubscriptionRequestDTO;
import com.driveden.app.domain.subscriptions.dto.ActivateSubscriptionResponseDTO;
import com.driveden.app.domain.subscriptions.dto.CurrentSubscriptionResponseDTO;
import com.driveden.app.domain.subscriptions.dto.SubscriptionPlanResponseDTO;
import com.driveden.app.domain.subscriptions.exception.InvalidSubscriptionStateException;
import com.driveden.app.domain.subscriptions.exception.SubscriptionLimitExceededException;
import com.driveden.app.domain.subscriptions.exception.SubscriptionNotFoundException;
import com.driveden.app.domain.subscriptions.model.Subscription;
import com.driveden.app.domain.subscriptions.model.SubscriptionPlan;
import com.driveden.app.domain.subscriptions.model.SubscriptionProvider;
import com.driveden.app.domain.subscriptions.model.SubscriptionStatus;
import com.driveden.app.domain.subscriptions.model.SubscriptionUsage;
import com.driveden.app.infrastructure.out.persistence.mappers.SubscriptionPlanMapper;
import com.driveden.app.infrastructure.out.persistence.mappers.UserSubscriptionMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private static final String FREE_PLAN_CODE = "FREE";

    private final UserSubscriptionRepositoryPort userSubscriptionRepository;
    private final SubscriptionPlanRepositoryPort subscriptionPlanRepository;
    private final SubscriptionUsageRepositoryPort subscriptionUsageRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final UsersService usersService;

    @Transactional
    public Subscription getActiveSubscription(Long userId) {
        usersService.findUserById(userId);
        LocalDateTime now = LocalDateTime.now();

        return userSubscriptionRepository.findActiveByUserId(userId, now)
                .filter(subscription -> subscription.isActiveAt(now))
                .orElseGet(() -> createFreeSubscription(userId, now));
    }

    public CurrentSubscriptionResponseDTO getCurrentSubscription(Long userId) {
        return UserSubscriptionMapper.toCurrentResponseDTO(getActiveSubscription(userId));
    }

    public List<SubscriptionPlanResponseDTO> getAvailablePlans() {
        return subscriptionPlanRepository.findAllActive().stream()
                .map(SubscriptionPlanMapper::toResponseDTO)
                .toList();
    }

    public boolean canCreateVehicle(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        Integer maxVehicles = subscription.getPlan().getMaxVehicles();

        if (maxVehicles == null) {
            return true;
        }

        return userVehicleRepository.countByUserId(userId) < maxVehicles;
    }

    @Transactional
    public boolean canUploadScan(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        SubscriptionUsage usage = getOrCreateCurrentUsage(subscription);
        Integer maxScans = subscription.getPlan().getMaxMonthlyScanImgs();

        return maxScans == null || usage.getScansUsed() < maxScans;
    }

    @Transactional
    public boolean canUseAudio(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        SubscriptionUsage usage = getOrCreateCurrentUsage(subscription);
        Integer maxAudios = subscription.getPlan().getMaxMonthlyAudios();

        return maxAudios == null || usage.getAudiosUsed() < maxAudios;
    }

    @Transactional
    public void enforceCanCreateVehicle(Long userId) {
        if (!canCreateVehicle(userId)) {
            throw new SubscriptionLimitExceededException("Vehicle limit exceeded for current subscription");
        }
    }

    @Transactional
    public void enforceCanUseAudio(Long userId) {
        if (!canUseAudio(userId)) {
            throw new SubscriptionLimitExceededException("Audio usage limit exceeded for current subscription");
        }
    }

    @Transactional
    public void consumeAudioUsage(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        SubscriptionUsage usage = getOrCreateCurrentUsage(subscription);

        usage.setAudiosUsed(usage.getAudiosUsed() + 1);
        subscriptionUsageRepository.save(usage);
    }

    @Transactional
    public void consumeScanUsage(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        SubscriptionUsage usage = getOrCreateCurrentUsage(subscription);

        usage.setScansUsed(usage.getScansUsed() + 1);
        subscriptionUsageRepository.save(usage);
    }

    public ActivateSubscriptionResponseDTO activate(ActivateSubscriptionRequestDTO request) {
        if (!SubscriptionProvider.GOOGLE_PLAY.name().equals(request.provider())) {
            throw new CustomException("Unsupported subscription provider", HttpStatus.BAD_REQUEST, "UNSUPPORTED_PROVIDER");
        }

        return new ActivateSubscriptionResponseDTO(
                request.provider(),
                "NOT_IMPLEMENTED",
                "Google Play subscription activation is not implemented yet"
        );
    }

    @Transactional
    public CurrentSubscriptionResponseDTO grantSubscription(Long userId, String planCode) {
        usersService.findUserById(userId);
        SubscriptionPlan plan = subscriptionPlanRepository.findActiveByCode(planCode)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription plan not found"));

        LocalDateTime now = LocalDateTime.now();
        Subscription subscription = userSubscriptionRepository.findActiveByUserId(userId, now)
                .map(existing -> updateGrantedSubscription(existing, plan, now))
                .orElseGet(() -> buildGrantedSubscription(userId, plan, now));

        Subscription savedSubscription = userSubscriptionRepository.save(subscription);
        getOrCreateCurrentUsage(savedSubscription);

        return UserSubscriptionMapper.toCurrentResponseDTO(savedSubscription);
    }

    private Subscription createFreeSubscription(Long userId, LocalDateTime now) {
        SubscriptionPlan freePlan = subscriptionPlanRepository.findActiveByCode(FREE_PLAN_CODE)
                .orElseThrow(() -> new SubscriptionNotFoundException("FREE subscription plan not found"));

        Subscription subscription = Subscription.builder()
                .userId(userId)
                .plan(freePlan)
                .provider(SubscriptionProvider.SYSTEM_FREE)
                .status(SubscriptionStatus.ACTIVE)
                .startsAt(now)
                .expiresAt(now.plusMonths(1))
                .autoRenew(true)
                .trial(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return userSubscriptionRepository.save(subscription);
    }

    private Subscription buildGrantedSubscription(Long userId, SubscriptionPlan plan, LocalDateTime now) {
        return Subscription.builder()
                .userId(userId)
                .plan(plan)
                .provider(SubscriptionProvider.ADMIN_GRANT)
                .status(SubscriptionStatus.ACTIVE)
                .startsAt(now)
                .expiresAt(now.plusMonths(1))
                .autoRenew(false)
                .trial(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private Subscription updateGrantedSubscription(
            Subscription subscription,
            SubscriptionPlan plan,
            LocalDateTime now
    ) {
        subscription.setPlan(plan);
        subscription.setProvider(SubscriptionProvider.ADMIN_GRANT);
        subscription.setProviderSubscriptionId(null);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartsAt(now);
        subscription.setExpiresAt(now.plusMonths(1));
        subscription.setAutoRenew(false);
        subscription.setTrial(false);
        subscription.setCancelledAt(null);
        subscription.setGracePeriodUntil(null);
        subscription.setUpdatedAt(now);
        return subscription;
    }

    private SubscriptionUsage getOrCreateCurrentUsage(Subscription subscription) {
        if (subscription.getId() == null) {
            throw new InvalidSubscriptionStateException("Subscription must be persisted before usage can be tracked");
        }

        LocalDateTime periodStart = subscription.getStartsAt();
        LocalDateTime periodEnd = subscription.getExpiresAt();
        if (periodStart == null || periodEnd == null) {
            throw new InvalidSubscriptionStateException("Subscription billing period is incomplete");
        }

        return subscriptionUsageRepository
                .findBySubscriptionIdAndPeriod(subscription.getId(), periodStart, periodEnd)
                .orElseGet(() -> subscriptionUsageRepository.save(SubscriptionUsage.builder()
                        .subscriptionId(subscription.getId())
                        .periodStart(periodStart)
                        .periodEnd(periodEnd)
                        .scansUsed(0)
                        .audiosUsed(0)
                        .build()));
    }
}
