package com.promo.management.system.promomanagement.model.enumeration;

import java.util.stream.Stream;

import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tenant {

    GREECE("greece"),
    JAPAN("japan");

    private final String tokenClaim;

    public static Tenant fromTokenClaim(String tokenClaim) {
        return Stream.of(Tenant.values())
            .filter(t -> t.tokenClaim.equals(tokenClaim))
            .findFirst()
            .orElseThrow(() -> new PromoSystemRuntimeException(PromoManagementSystemError.UNEXPECTED_TENANT));
    }

}
