package com.promo.management.system.promomanagement.web.validation;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import com.promo.management.system.promomanagement.web.model.dto.request.UpdatePromoRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdatePromoRequestConstraintValidator implements ConstraintValidator<UpdatePromoRequestConstraint, UpdatePromoRequestDto> {

    @Override
    public boolean isValid(UpdatePromoRequestDto request, ConstraintValidatorContext context) {
        return nonNull(request) && !(nonNull(request.getDiscountValue()) && nonNull(request.getDiscountPercentage()))
            && atLeastOne(request);
    }

    private static boolean atLeastOne(UpdatePromoRequestDto request) {
        return hasText(request.getCode()) || nonNull(request.getDiscountValue())
            || nonNull(request.getDiscountPercentage()) || nonNull(request.getStatus())
            || nonNull(request.getUsageLimit()) || nonNull(request.getExpiryDate());
    }

}
