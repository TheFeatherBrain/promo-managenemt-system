package com.promo.management.system.promomanagement.web.validation;

import static java.util.Objects.nonNull;

import com.promo.management.system.promomanagement.web.model.dto.request.CreatePromoRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreatePromoRequestConstraintValidator implements ConstraintValidator<CreatePromoRequestConstraint, CreatePromoRequestDto> {

    @Override
    public boolean isValid(CreatePromoRequestDto request, ConstraintValidatorContext context) {
        return nonNull(request) && (nonNull(request.getDiscountValue()) || nonNull(request.getDiscountPercentage()));
    }

}
