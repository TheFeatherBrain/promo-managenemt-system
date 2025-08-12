package com.promo.management.system.promomanagement.web.validation;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.web.model.dto.request.CreatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.UpdatePromoRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdatePromoRequestConstraintValidator implements ConstraintValidator<UpdatePromoRequestConstraint, UpdatePromoRequestDto> {

    @Override
    public boolean isValid(UpdatePromoRequestDto request, ConstraintValidatorContext context) {
        return nonNull(request) && validDiscount(request) && validateDateAndStatus(request);
    }

    private static boolean validDiscount(UpdatePromoRequestDto request) {
        boolean isDiscountValue = nonNull(request.getDiscountValue());
        boolean isDiscountPercentage = nonNull(request.getDiscountPercentage());

        return !(isDiscountValue == isDiscountPercentage);
    }

    private static boolean validateDateAndStatus(UpdatePromoRequestDto request) {
        if (isNull(request.getExpiryDate())) {
            return true;
        }
        boolean isPast = request.getExpiryDate().isBefore(LocalDateTime.now());
        boolean isExpired = PromoCodeStatus.EXPIRED.equals(request.getStatus());

        return isExpired == isPast;
    }

}
