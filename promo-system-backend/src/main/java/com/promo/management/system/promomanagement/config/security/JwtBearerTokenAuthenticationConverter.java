package com.promo.management.system.promomanagement.config.security;

import java.util.Collection;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtBearerTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLES = "roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<String> roles = jwt.getClaim(ROLES);
        Collection<GrantedAuthority> grantedAuthorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, grantedAuthorities);
    }

}
