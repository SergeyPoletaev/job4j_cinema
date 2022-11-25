package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.App;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TicketDbStoreTest {
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
        SessionDbStore sessionDbStore = new SessionDbStore(pool);
        sessionDbStore.add(session1);
        sessionDbStore.add(session2);
        User user = new User(1, "anna", "anna@", "123");
        UserDbStore userDbStore = new UserDbStore(pool);
        userDbStore.add(user);
        Ticket ticket1 = new Ticket(1, session1, 2, 3, user);
        Ticket ticket2 = new Ticket(2, session2, 4, 5, user);
        TicketDbStore ticketDbStore = new TicketDbStore(pool);
        ticketDbStore.add(ticket1);
        ticketDbStore.add(ticket2);
        assertThat(ticketDbStore.findAll()).isEqualTo(List.of(ticket1, ticket2));
    }
}