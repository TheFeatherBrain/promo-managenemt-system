package com.promo.management.system.promomanagement.web.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreatePromoRequestConstraintValidator.class)
public @interface CreatePromoRequestConstraint {

    String message() default "At least one of the discount value or percentage must be provided.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
