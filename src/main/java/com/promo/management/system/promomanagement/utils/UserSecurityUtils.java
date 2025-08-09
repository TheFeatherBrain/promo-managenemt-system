package com.promo.management.system.promomanagement.utils;

import java.util.Optional;

import com.promo.management.system.promomanagement.model.enumeration.Tenant;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@UtilityClass
public class UserSecurityUtils {

    private final String TENANT = "tenant";

    public static Tenant getTenant() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = authentication instanceof JwtAuthenticationToken ? ((JwtAuthenticationToken) authentication).getToken() : null;
        return Optional.ofNullable(jwt)
            .map(token -> token.getClaimAsString(TENANT))
            .map(Tenant::fromDescription)
            .orElse(null);
    }

    public static String getLoggedInUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
