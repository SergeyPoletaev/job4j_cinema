package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.App;
import ru.job4j.cinema.model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SessionDbStoreTest {
    private static BasicDataSource pool;

    @BeforeAll
    static void initPoll() {
        pool = new App().loadPool();
    }

    @AfterAll
    static void close() throws SQLException {
        pool.close();
    }

    @BeforeEach
    void clearDb() throws SQLException {
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from sessions")) {
            ps.executeUpdate();
        }
    }

    @Test
    void whenFindAllSessions() {
        Session session1 = new Session(1, "one");
        Session session2 = new Session(2, "two");
        SessionDbStore sessionDbStore = new SessionDbStore(pool);
        sessionDbStore.add(session1);
        sessionDbStore.add(session2);
        assertThat(sessionDbStore.findAll()).isEqualTo(List.of(session1, session2));
    }

}