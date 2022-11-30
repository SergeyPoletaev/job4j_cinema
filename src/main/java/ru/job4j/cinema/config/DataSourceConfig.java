package ru.job4j.cinema.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
public class DataSourceConfig {

    @Bean
    public DataSource loadPool(@Value("${jdbc.driver}") String driver,
                               @Value("${jdbc.url}") String url,
                               @Value("${jdbc.username}") String username,
                               @Value("${jdbc.password}") String password) {
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName(driver);
        pool.setUrl(url);
        pool.setUsername(username);
        pool.setPassword(password);
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
        return pool;
    }

    public Properties loadDbProperties() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(DataSourceConfig.class.getClassLoader()
                                .getResourceAsStream("db.properties"))
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return cfg;
    }
}
