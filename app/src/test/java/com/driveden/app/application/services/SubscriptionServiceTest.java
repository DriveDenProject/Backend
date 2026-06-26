package com.driveden.app.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.driveden.app.application.ports.out.SubscriptionPlanRepositoryPort;
import com.driveden.app.application.ports.out.SubscriptionUsageRepositoryPort;
import com.driveden.app.application.ports.out.UserSubscriptionRepositoryPort;
import com.driveden.app.domain.subscriptions.dto.CurrentSubscriptionResponseDTO;
import com.driveden.app.domain.subscriptions.model.Subscription;
import com.driveden.app.domain.subscriptions.model.SubscriptionPlan;
import com.driveden.app.domain.subscriptions.model.SubscriptionProvider;
import com.driveden.app.domain.subscriptions.model.SubscriptionStatus;
import com.driveden.app.domain.subscriptions.model.SubscriptionUsage;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;

class SubscriptionServiceTest {

    private UserSubscriptionRepositoryPort userSubscriptionRepository;
    private SubscriptionPlanRepositoryPort subscriptionPlanRepository;
    private SubscriptionUsageRepositoryPort subscriptionUsageRepository;
    private UserVehicleRepository userVehicleRepository;
    private UsersService usersService;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        userSubscriptionRepository = mock(UserSubscriptionRepositoryPort.class);
        subscriptionPlanRepository = mock(SubscriptionPlanRepositoryPort.class);
        subscriptionUsageRepository = mock(SubscriptionUsageRepositoryPort.class);
        userVehicleRepository = mock(UserVehicleRepository.class);
        usersService = mock(UsersService.class);

        subscriptionService = new SubscriptionService(
                userSubscriptionRepository,
                subscriptionPlanRepository,
                subscriptionUsageRepository,
                userVehicleRepository,
                usersService
        );
    }

    @Test
    void freeUserCannotExceedVehicleLimit() {
        when(userSubscriptionRepository.findActiveByUserId(any(), any()))
                .thenReturn(Optional.of(subscription(freePlan(), 1L)));
        when(userVehicleRepository.countByUserId(7L)).thenReturn(1L);

        assertThat(subscriptionService.canCreateVehicle(7L)).isFalse();
    }

    @Test
    void proUserCanExceedFreeVehicleLimit() {
        when(userSubscriptionRepository.findActiveByUserId(any(), any()))
                .thenReturn(Optional.of(subscription(proPlan(), 1L)));
        when(userVehicleRepository.countByUserId(7L)).thenReturn(1L);

        assertThat(subscriptionService.canCreateVehicle(7L)).isTrue();
    }

    @Test
    void usageCountersEnforceScanAndAudioQuotas() {
        Subscription subscription = subscription(freePlan(), 11L);
        SubscriptionUsage usage = usage(subscription, 5, 4);

        when(userSubscriptionRepository.findActiveByUserId(any(), any()))
                .thenReturn(Optional.of(subscription));
        when(subscriptionUsageRepository.findBySubscriptionIdAndPeriod(
                subscription.getId(),
                subscription.getStartsAt(),
                subscription.getExpiresAt()
        )).thenReturn(Optional.of(usage));

        assertThat(subscriptionService.canUploadScan(7L)).isFalse();
        assertThat(subscriptionService.canUseAudio(7L)).isTrue();
    }

    @Test
    void adminGrantCreatesActiveSubscriptionAndInitializesUsage() {
        SubscriptionPlan proPlan = proPlan();
        Subscription savedSubscription = subscription(proPlan, 21L);
        savedSubscription.setProvider(SubscriptionProvider.ADMIN_GRANT);

        when(subscriptionPlanRepository.findActiveByCode("PRO")).thenReturn(Optional.of(proPlan));
        when(userSubscriptionRepository.findActiveByUserId(any(), any())).thenReturn(Optional.empty());
        when(userSubscriptionRepository.save(any(Subscription.class))).thenReturn(savedSubscription);
        when(subscriptionUsageRepository.findBySubscriptionIdAndPeriod(
                savedSubscription.getId(),
                savedSubscription.getStartsAt(),
                savedSubscription.getExpiresAt()
        )).thenReturn(Optional.empty());
        when(subscriptionUsageRepository.save(any(SubscriptionUsage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CurrentSubscriptionResponseDTO response = subscriptionService.grantSubscription(7L, "PRO");

        assertThat(response.plan()).isEqualTo("PRO");
        assertThat(response.status()).isEqualTo("ACTIVE");
        verify(subscriptionUsageRepository).save(any(SubscriptionUsage.class));
    }

    private Subscription subscription(SubscriptionPlan plan, Long subscriptionId) {
        LocalDateTime now = LocalDateTime.now();
        return Subscription.builder()
                .id(subscriptionId)
                .userId(7L)
                .plan(plan)
                .provider(SubscriptionProvider.SYSTEM_FREE)
                .status(SubscriptionStatus.ACTIVE)
                .startsAt(now.minusDays(1))
                .expiresAt(now.plusDays(29))
                .autoRenew(true)
                .trial(false)
                .build();
    }

    private SubscriptionUsage usage(Subscription subscription, int scansUsed, int audiosUsed) {
        return SubscriptionUsage.builder()
                .id(31L)
                .subscriptionId(subscription.getId())
                .periodStart(subscription.getStartsAt())
                .periodEnd(subscription.getExpiresAt())
                .scansUsed(scansUsed)
                .audiosUsed(audiosUsed)
                .build();
    }

    private SubscriptionPlan freePlan() {
        return plan(1L, "FREE", 1, 5, 5, BigDecimal.ZERO);
    }

    private SubscriptionPlan proPlan() {
        return plan(3L, "PRO", 9999, 9999, 9999, BigDecimal.valueOf(9.99));
    }

    private SubscriptionPlan plan(
            Long id,
            String code,
            Integer maxVehicles,
            Integer maxScans,
            Integer maxAudios,
            BigDecimal monthlyPrice
    ) {
        return SubscriptionPlan.builder()
                .id(id)
                .code(code)
                .name("DriveDen " + code)
                .monthlyPrice(monthlyPrice)
                .currency("USD")
                .maxVehicles(maxVehicles)
                .maxMonthlyScanImgs(maxScans)
                .maxMonthlyAudios(maxAudios)
                .active(true)
                .build();
    }
}
