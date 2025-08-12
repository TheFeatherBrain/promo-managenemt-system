package com.promo.management.system.promomanagement.model.enumeration;

import java.util.stream.Stream;

import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromoCodeStatus {

    ACTIVE(1),
    EXPIRED(2),
    DISABLED(3);

    private final int code;

    public static PromoCodeStatus ofCode(int code) {
        return Stream.of(PromoCodeStatus.values())
            .filter(promo -> promo.getCode() == code)
            .findFirst()
            .orElseThrow(() -> new PromoSystemRuntimeException(PromoManagementSystemError.UNEXPECTED_PROMO_CODE_STATUS));
    }

}
