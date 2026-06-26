package com.driveden.app.infrastructure.out.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subscription_plans")
public class SubscriptionPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "monthly_price")
    private BigDecimal monthlyPrice;

    @Column(name = "yearly_price")
    private BigDecimal yearlyPrice;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "max_vehicles")
    private Integer maxVehicles;

    @Column(name = "max_scan_imgs")
    private Integer maxMonthlyScanImgs;

    @Column(name = "max_audios")
    private Integer maxMonthlyAudios;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "plan_features",
            joinColumns = @JoinColumn(name = "subscription_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<SubscriptionFeatureEntity> features = new HashSet<>();
}
