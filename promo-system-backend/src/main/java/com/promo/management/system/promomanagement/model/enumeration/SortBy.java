package com.promo.management.system.promomanagement.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortBy {

    CODE("code"),
    EXPIRATION_DATE("expiryDate");

    private final String column;
}
