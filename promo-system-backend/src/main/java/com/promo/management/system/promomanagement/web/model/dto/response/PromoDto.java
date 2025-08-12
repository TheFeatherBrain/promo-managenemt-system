package com.promo.management.system.promomanagement.web.model.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PromoDto {

    private UUID id;
    private String code;
    private Integer discountValue;
    private Integer discountPercentage;
    private LocalDateTime expiryDate;
    private Integer usageLimit;
    private Integer usages;
    private PromoCodeStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

}
