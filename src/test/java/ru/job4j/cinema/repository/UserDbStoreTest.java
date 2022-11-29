package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.App;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class UserDbStoreTest {
    private static BasicDataSource pool;

    @BeforeAll
    static void initPool() {
        pool = new App().loadPool();
    }

    @AfterAll
    static void close() throws SQLException {
        pool.close();
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