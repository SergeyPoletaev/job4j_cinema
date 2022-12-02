package ru.job4j.cinema.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class TestDataSourceConfig {

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
