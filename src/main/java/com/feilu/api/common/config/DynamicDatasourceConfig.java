package com.feilu.api.common.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dzh
 */
@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DynamicDatasourceConfig {

    @Bean("system")
    @ConfigurationProperties(prefix = "spring.datasource.system")
    public DataSource systemDatasource(){
        return DataSourceBuilder.create().build();
    }
    @Bean("site")
    @ConfigurationProperties(prefix = "spring.datasource.site")
    public DataSource siteDatasource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DataSource dataSource(){
        Map<Object, Object> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("system", systemDatasource());
        dataSourceMap.put("site", siteDatasource());

        DynamicDatasource dynamicDatasource = new DynamicDatasource();
        dynamicDatasource.setTargetDataSources(dataSourceMap);
        dynamicDatasource.setDefaultTargetDataSource(systemDatasource());
        return dynamicDatasource;
    }
}
