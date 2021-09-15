package com.mthoko.mobile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    public static final String PRODUCTION = "production";

    @Bean
    public DataSource getDataSource(
            @Value("${datasource.driver-class-name}") String driverClassName,
            @Value("${datasource.url}") String url,
            @Value("${datasource.username}") String username,
            @Value("${datasource.password}") String password,
            @Value("${dev.datasource.driver-class-name}") String devDriverClassName,
            @Value("${dev.datasource.url}") String devUrl,
            @Value("${dev.datasource.username}") String devUsername,
            @Value("${dev.datasource.password}") String devPassword,
            @Value("${spring.profiles.active}") String activeProfile
    ) {
        if (!PRODUCTION.equals(activeProfile)) {
            return getDataSourceFromProperties(devDriverClassName, devUrl, devUsername, devPassword);
        }
        return getDataSourceFromProperties(driverClassName, url, username, password);

    }

    private DataSource getDataSourceFromProperties(String driverClassName, String url, String username, String password) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}