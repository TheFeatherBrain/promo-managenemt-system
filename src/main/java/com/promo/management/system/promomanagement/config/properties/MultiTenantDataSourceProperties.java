package com.promo.management.system.promomanagement.config.properties;

import java.util.HashMap;
import java.util.Map;

import com.promo.management.system.promomanagement.model.enumeration.TenantEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Multi tenant datasource properties.
 */
@Data
@ConfigurationProperties("spring.datasources")
public class MultiTenantDataSourceProperties {

    private Map<TenantEnum, DataSourceProperties> tenantDatasourceProperties = new HashMap<>();

}
