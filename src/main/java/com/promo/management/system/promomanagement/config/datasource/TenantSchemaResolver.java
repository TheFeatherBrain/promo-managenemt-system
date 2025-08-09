package com.promo.management.system.promomanagement.config.datasource;

import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.UNEXPECTED_TENANT;
import static java.util.Objects.nonNull;

import java.util.Map;

import com.promo.management.system.promomanagement.config.properties.MultiTenantProperties;
import com.promo.management.system.promomanagement.model.enumeration.Tenant;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeException;
import com.promo.management.system.promomanagement.utils.UserSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    private final MultiTenantProperties properties;

    private static final String PUBLIC_SCHEMA = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        Tenant tenant = UserSecurityUtils.getTenant();

        return nonNull(tenant)
            ? properties.getTenants().stream()
                .filter(t -> t.getName().equals(tenant))
                .map(MultiTenantProperties.TenantInfo::getSchema)
                .findFirst()
                .orElseThrow(() -> new PromoSystemRuntimeException(UNEXPECTED_TENANT))
            : PUBLIC_SCHEMA;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

}
