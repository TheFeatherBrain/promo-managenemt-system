package com.promo.management.system.promomanagement.config.datasource;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flyway configuration to handle multiple databases.
 */
@Configuration
public class FlywayConfig {

    /**
     * Flyway configuration.
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy(DataSource dataSource) {
        return flyway -> {

//            Flyway.configure()
//                .schemas("sales")
//                .dataSource(linkDataSource)
//                .locations("db/migration/link")
//                .baselineOnMigrate(true)
//                .baselineVersion("0")
//                .load()
//                .migrate();

            Flyway.configure()
                .schemas("promo")
                .dataSource(dataSource)
                .locations("migration")
                .baselineOnMigrate(false)
                .load()
                .migrate();
        };
    }

}
