package com.courses.management.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnector {
    private  final HikariDataSource ds;
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnector.class);


    public DatabaseConnector() {
        HikariConfig config = new HikariConfig();
        final Properties properties = new Properties();
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            LOGGER.error("Error loading application.properties", e);
            throw new RuntimeException("");
        }

        try {
            Class.forName(properties.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Error loading postgres driver", ex);
            throw new RuntimeException("Error loading postgres driver", ex);
        }

        config.setJdbcUrl(properties.getProperty("jdbc.url"));
        config.setUsername(properties.getProperty("jdbc.username"));
        config.setPassword(properties.getProperty("jdbc.password"));
        ds = new HikariDataSource(config);
        ds.setMaximumPoolSize(Integer.parseInt(properties.getProperty("jdbc.connection.pool.max.size")));
    }

    public DataSource getDataSource() {
        return ds;
    }
}
