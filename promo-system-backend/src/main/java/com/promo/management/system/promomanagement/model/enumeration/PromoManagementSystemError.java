package com.promo.management.system.promomanagement.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromoManagementSystemError {

    SERVER_ERROR("PMS-1000", "PMS Server Error"),
    UNEXPECTED_PROMO_CODE_STATUS("PMS-1001", "Unexpected promo code status."),
    PROMO_CODE_NOT_FOUND("PMS-1002", "Promo code does not exist."),
    PROMO_CODE_ALREADY_EXISTS("PMS-1003", "Promo code already exists."),
    UNEXPECTED_TENANT("PMS-1004", "Unexpected tenant"),
    PROMO_CODE_IS_EXPIRED("PMS-1005", "Promo code has expired."),
    PROMO_CODE_IS_INVALID("PMS-1006", "Promo code is invalid."),
    PROMO_CODE_HAS_EXCEED_ITS_USAGE_LIMIT("PMS-1007", "Promo code has exceed its usage limit.");

    private final String code;
    private final String message;

}
