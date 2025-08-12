package com.promo.management.system.promomanagement.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promo_code")
public class PromoCode {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue
    private UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "discount_value")
    private Integer discountValue;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "usages")
    private Integer usages;

    @Column(name = "status", nullable = false)
    private PromoCodeStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

}
