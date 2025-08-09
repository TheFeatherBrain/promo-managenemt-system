package com.promo.management.system.promomanagement.config.properties;

import java.util.Collections;
import java.util.List;

import com.promo.management.system.promomanagement.model.enumeration.Tenant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("pms.multi-tenant")
public class MultiTenantProperties {

    private List<TenantInfo> tenants = Collections.emptyList();

    @Getter
    @Setter
    public static class TenantInfo {
        private Tenant name;
        private String schema;
    }

}
