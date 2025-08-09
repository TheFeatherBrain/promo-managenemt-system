package com.promo.management.system.promomanagement.web.validation;

import static java.util.Objects.nonNull;

import com.promo.management.system.promomanagement.model.dto.CreatePromoCodeRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreatePromoRequestConstraintValidator implements ConstraintValidator<CreatePromoRequestConstraint, CreatePromoCodeRequestDto> {

    @Override
    public boolean isValid(CreatePromoCodeRequestDto request, ConstraintValidatorContext context) {
        return nonNull(request) && (nonNull(request.getDiscountValue()) || nonNull(request.getDiscountPercentage()));
    }

}
