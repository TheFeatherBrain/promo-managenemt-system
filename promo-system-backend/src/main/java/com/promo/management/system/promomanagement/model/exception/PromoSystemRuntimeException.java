package com.promo.management.system.promomanagement.model.exception;

import com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError;
import lombok.Getter;

@Getter
public class PromoSystemRuntimeException extends RuntimeException {

    private final String code;
    private final String message;

    public PromoSystemRuntimeException(final PromoManagementSystemError promoManagementSystemError) {
        this(promoManagementSystemError.getCode(), promoManagementSystemError.getMessage());
    }

    public PromoSystemRuntimeException(final String code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
