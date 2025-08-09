package com.promo.management.system.promomanagement.config.datasource;

import static com.promo.management.system.promomanagement.model.enumeration.Tenant.GREECE;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiTenantDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return GREECE;
    }

}
