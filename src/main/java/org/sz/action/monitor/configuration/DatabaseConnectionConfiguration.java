package org.sz.action.monitor.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class provides data source configuration for the {@link org.sz.action.monitor.listener.PostgreSqlDatabaseListener}.
 */
@Configuration
public class DatabaseConnectionConfiguration {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUser;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    /**
     * Datasource bean for the {@link org.sz.action.monitor.listener.PostgreSqlDatabaseListener}.
     *
     * @return Connection object for the data source provided in application.properties
     * @throws SQLException is thrown if connection cannot be retrieved
     */
    @Bean
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dataSourceUrl, dataSourceUser, dataSourcePassword);
    }


}
