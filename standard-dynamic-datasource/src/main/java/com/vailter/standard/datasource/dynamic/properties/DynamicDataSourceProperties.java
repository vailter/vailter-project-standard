package com.vailter.standard.datasource.dynamic.properties;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 多数据源属性
 *
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "dynamic")
public class DynamicDataSourceProperties {
    private Map<String, DruidDataSource> datasource = new LinkedHashMap<>();

    public Map<String, DruidDataSource> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, DruidDataSource> datasource) {
        this.datasource = datasource;
    }
}
