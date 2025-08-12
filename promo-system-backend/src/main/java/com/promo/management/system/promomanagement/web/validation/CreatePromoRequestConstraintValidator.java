package com.promo.management.system.promomanagement.web.validation;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.time.LocalDateTime;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.web.model.dto.request.CreatePromoRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreatePromoRequestConstraintValidator implements ConstraintValidator<CreatePromoRequestConstraint, CreatePromoRequestDto> {

    @Override
    public boolean isValid(CreatePromoRequestDto request, ConstraintValidatorContext context) {
        return nonNull(request) && validDiscount(request) && futureExpirationDate(request)
            && !PromoCodeStatus.EXPIRED.equals(request.getStatus());
    }

    private static boolean validDiscount(CreatePromoRequestDto request) {
        boolean isDiscountValue = nonNull(request.getDiscountValue());
        boolean isDiscountPercentage = nonNull(request.getDiscountPercentage());

        return !(isDiscountValue == isDiscountPercentage);
    }

    private static boolean futureExpirationDate(CreatePromoRequestDto request) {
        return isNull(request.getExpiryDate()) || request.getExpiryDate().isAfter(LocalDateTime.now());
    }

}
