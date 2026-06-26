package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driveden.app.domain.subscriptions.model.SubscriptionStatus;
import com.driveden.app.infrastructure.out.persistence.entity.UserSubscriptionEntity;

public interface UserSubscriptionJpa extends JpaRepository<UserSubscriptionEntity, Long> {

    @Query("""
        SELECT subscription
        FROM UserSubscriptionEntity subscription
        JOIN FETCH subscription.plan
        WHERE subscription.user.id = :userId
          AND subscription.status = :status
          AND subscription.startsAt <= :now
          AND (subscription.expiresAt IS NULL OR subscription.expiresAt > :now)
          AND (
                subscription.cancelledAt IS NULL
                OR (
                    subscription.gracePeriodUntil IS NOT NULL
                    AND subscription.gracePeriodUntil >= :now
                )
          )
        ORDER BY subscription.startsAt DESC
    """)
    List<UserSubscriptionEntity> findActiveByUserId(
            @Param("userId") Long userId,
            @Param("status") SubscriptionStatus status,
            @Param("now") LocalDateTime now
    );
}
