package com.mthoko.mobile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    
    @Bean
    public DataSource getDataSource(
    		@Value("${datasource.driver-class-name}") String driverClassName, 
    		@Value("${datasource.url}") String url, 
    		@Value("${datasource.username}") String username, 
    		@Value("${datasource.password}") String password
    		) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(driverClassName);
		dataSourceBuilder.url(url);
		dataSourceBuilder.username(username);
		dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}