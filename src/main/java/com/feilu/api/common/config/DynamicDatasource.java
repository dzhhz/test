package com.feilu.api.common.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * @author dzh
 */
public class DynamicDatasource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDatasourceHolder.getDataSource();
    }
}
