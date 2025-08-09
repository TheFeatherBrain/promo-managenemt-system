package com.promo.management.system.promomanagement.config.properties;

import java.util.HashMap;
import java.util.Map;

import com.promo.management.system.promomanagement.model.enumeration.Tenant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties("spring.multi-tenant")
public class MultiTenantDataSourceProperties {

    private Map<Tenant, DataSourceProperties> dataSources = new HashMap<>();

}
