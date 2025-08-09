package com.promo.management.system.promomanagement.model.dto;

import java.time.LocalDateTime;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.web.validation.UpdatePromoRequestConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@UpdatePromoRequestConstraint
public class UpdatePromoRequestDto {

    private String code;
    private Integer discountValue;
    private Integer discountPercentage;
    private LocalDateTime expiryDate;
    private Integer usageLimit;
    private PromoCodeStatus status;

}
