package com.promo.management.system.promomanagement.model.exception;

import com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError;
import lombok.Getter;

@Getter
public class PromoSystemRuntimeValidationException extends PromoSystemRuntimeException {

    public PromoSystemRuntimeValidationException(final PromoManagementSystemError error) {
        super(error);
    }

}
