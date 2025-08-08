package com.promo.management.system.promomanagement.config.datasource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy(MultiTenantDataSource dataSource) {
        return flyway -> dataSource.getResolvedDataSources().forEach((key, ds) ->
            Flyway.configure()
                .schemas("promo")
                .dataSource(ds)
                .locations("migration")
                .baselineOnMigrate(false)
                .load()
                .migrate());
    }

}
