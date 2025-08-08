package com.promo.management.system.promomanagement.config.datasource;

import static com.promo.management.system.promomanagement.model.enumeration.TenantEnum.GREECE;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Configuration
public class MultiTenantDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return GREECE;
    }

}
