package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfig;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class UserDbStoreTest {
    private static DataSource pool;

    @BeforeAll
    static void initPool() {
        DataSourceConfig config = new DataSourceConfig();
        Properties prop = config.loadDbProperties();
        pool = config.loadPool(
                prop.getProperty("jdbc.driver"),
                prop.getProperty("jdbc.url"),
                prop.getProperty("jdbc.username"),
                prop.getProperty("jdbc.password")
        );
    }

    @AfterAll
    static void close() throws SQLException {
        ((BasicDataSource) pool).close();
    }

    @AfterEach
    void clearDb() throws SQLException {
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from users")) {
            ps.executeUpdate();
        }
    }

    @Test
    void whenAddUser() {
        UserDbStore userDbStore = new UserDbStore(pool);
        User user = new User(0, "anna", "anna@", "123");
        userDbStore.add(user);
        User userDb = userDbStore.findUserByEmailAndPhone(
                user.getEmail(),
                user.getPhone()).orElseThrow();
        assertThat(userDb).isEqualTo(user);
    }
}