package com.promo.management.system.promomanagement.config.datasource;

import javax.sql.DataSource;

import com.promo.management.system.promomanagement.config.properties.MultiTenantProperties;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy(MultiTenantProperties properties, DataSource dataSource) {
        return flyway -> properties.getTenants().forEach(tenant ->
            Flyway.configure()
                .schemas(tenant.getSchema())
                .dataSource(dataSource)
                .locations("migration")
                .baselineOnMigrate(false)
                .load()
                .migrate());
    }

}
