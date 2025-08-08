package com.promo.management.system.promomanagement.config.datasource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import com.promo.management.system.promomanagement.config.properties.MultiTenantDataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfig {

    @Bean
    public DataSource dataSource(MultiTenantDataSourceProperties dataSourceProperties) {
        Map<Object, Object> targetDataSources = new HashMap<>();

        dataSourceProperties.getTenantDatasourceProperties().forEach((id, properties) ->
            targetDataSources.put(id, properties.initializeDataSourceBuilder().build()));

        MultiTenantDataSource multiTenantDataSourceConfig = new MultiTenantDataSource();
        multiTenantDataSourceConfig.setTargetDataSources(targetDataSources);
        return multiTenantDataSourceConfig;
    }

}
