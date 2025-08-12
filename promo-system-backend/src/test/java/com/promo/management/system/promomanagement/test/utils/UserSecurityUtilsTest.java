package com.promo.management.system.promomanagement.test.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.promo.management.system.promomanagement.model.enumeration.Tenant;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeException;
import com.promo.management.system.promomanagement.utils.UserSecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class UserSecurityUtilsTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getTenant() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("tenant")).thenReturn("greece");

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Tenant tenant = UserSecurityUtils.getTenant();

        assertThat(tenant).isEqualTo(Tenant.GREECE);
        verify(jwt).getClaimAsString("tenant");
    }

    @Test
    void getTenant_returnsNull() {
        SecurityContextHolder.getContext()
            .setAuthentication(new TestingAuthenticationToken("bob", "pw"));

        Tenant tenant = UserSecurityUtils.getTenant();

        assertThat(tenant).isNull();
    }

    @Test
    void getTenant_returnsNull_claim_missing() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("tenant")).thenReturn(null);

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        Tenant tenant = UserSecurityUtils.getTenant();

        assertThat(tenant).isNull();
        verify(jwt).getClaimAsString("tenant");
    }

    @Test
    void getTenant__throws() {
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getClaimAsString("tenant")).thenReturn("spain"); // not in enum

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        assertThatThrownBy(UserSecurityUtils::getTenant)
            .isInstanceOf(PromoSystemRuntimeException.class);
    }

    @Test
    void getLoggedInUser() {
        SecurityContextHolder.getContext()
            .setAuthentication(new TestingAuthenticationToken("alice", "pw"));

        String username = UserSecurityUtils.getLoggedInUser();

        assertThat(username).isEqualTo("alice");
    }
}
