package com.driveden.app.domain.subscriptions.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUsage {

    private Long id;
    private Long subscriptionId;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private Integer scansUsed;
    private Integer audiosUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
