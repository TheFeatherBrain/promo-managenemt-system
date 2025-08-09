package com.promo.management.system.promomanagement.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromoManagementSystemError {

    SERVER_ERROR("PMS-1000", "PMS Server Error"),
    UNEXPECTED_PROMO_CODE_STATUS("PMS-1001", "Unexpected Promo Code Status"),
    PROMO_CODE_NOT_FOUND("PMS-1002", "Promo Code Not Found"),
    PROMO_CODE_ALREADY_EXISTS("PMS-1003", "Promo Code Already Exists");

    private final String code;
    private final String message;

}
