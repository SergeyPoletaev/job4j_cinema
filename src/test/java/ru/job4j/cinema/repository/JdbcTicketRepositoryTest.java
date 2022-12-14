package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfig;
import ru.job4j.cinema.config.TestDataSourceConfig;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTicketRepositoryTest {
    private static DataSource pool;

    @BeforeAll
    static void initPool() {
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

    @AfterEach
    void clearDb() throws SQLException {
        try (Connection conn = pool.getConnection();
             PreparedStatement ps1 = conn.prepareStatement("delete from tickets")) {
            PreparedStatement ps2 = conn.prepareStatement("delete from sessions");
            PreparedStatement ps3 = conn.prepareStatement("delete from users");
            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
        }
    }

    @Test
    void whenFindAllTickets() {
        Session session1 = new Session(1, "one");
        Session session2 = new Session(2, "two");
        SessionRepository sessionRepository = new JdbcSessionRepository(pool);
        sessionRepository.add(session1);
        sessionRepository.add(session2);
        User user = new User(1, "anna", "anna@", "123");
        UserRepository userRepository = new JdbcUserRepository(pool);
        userRepository.add(user);
        Ticket ticket1 = new Ticket(session1.getId(), 2, 3, user.getId());
        Ticket ticket2 = new Ticket(session2.getId(), 4, 5, user.getId());
        TicketRepository ticketRepository = new JdbcTicketRepository(pool);
        ticketRepository.add(ticket1);
        ticketRepository.add(ticket2);
        assertThat(ticketRepository.findAll()).isEqualTo(List.of(ticket1, ticket2));
    }
}