package com.promo.management.system.promomanagement.config.properties;

import java.util.HashMap;
import java.util.Map;

import com.promo.management.system.promomanagement.model.enumeration.TenantEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Multi tenant datasource properties.
 */
@Setter
@Getter
@ConfigurationProperties("spring.multi-tenant")
public class MultiTenantDataSourceProperties {

    private Map<TenantEnum, DataSourceProperties> dataSources = new HashMap<>();

}
