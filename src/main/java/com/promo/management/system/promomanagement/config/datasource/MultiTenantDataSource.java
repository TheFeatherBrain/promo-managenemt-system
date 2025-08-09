package com.promo.management.system.promomanagement.config.datasource;

import com.promo.management.system.promomanagement.utils.UserSecurityUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiTenantDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return UserSecurityUtils.getTenantFromClaim();
    }

}
