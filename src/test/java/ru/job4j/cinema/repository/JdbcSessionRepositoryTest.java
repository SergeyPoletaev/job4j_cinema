package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfig;
import ru.job4j.cinema.config.TestDataSourceConfig;
import ru.job4j.cinema.model.Session;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcSessionRepositoryTest {
    private static DataSource pool;

    @BeforeAll
    static void initPoll() {
        Properties prop = new TestDataSourceConfig().loadDbProperties();
        pool = new DataSourceConfig().loadPool(
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
        SessionRepository sessionRepository = new JdbcSessionRepository(pool);
        sessionRepository.add(session1);
        sessionRepository.add(session2);
        assertThat(sessionRepository.findAll()).isEqualTo(List.of(session1, session2));
    }

}