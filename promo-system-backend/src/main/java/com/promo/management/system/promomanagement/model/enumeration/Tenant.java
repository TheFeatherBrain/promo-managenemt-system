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

    private final String description;

    public static Tenant fromDescription(String tokenClaim) {
        return Stream.of(Tenant.values())
            .filter(t -> t.description.equals(tokenClaim))
            .findFirst()
            .orElseThrow(() -> new PromoSystemRuntimeException(PromoManagementSystemError.UNEXPECTED_TENANT));
    }

}
