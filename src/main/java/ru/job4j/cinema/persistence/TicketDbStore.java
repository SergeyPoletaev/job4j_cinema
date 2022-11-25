package ru.job4j.cinema.persistence;

import net.jcip.annotations.ThreadSafe;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Repository
public class TicketDbStore {
    private static final Logger LOG = LoggerFactory.getLogger(TicketDbStore.class.getName());
    private static final String INSERT_INTO_TICKET = "insert into tickets (session_id, pos_row, cell, user_id) values (?, ?, ?, ?)";
    private static final String SELECT_ALL_TICKET = "select * from tickets";
    private final BasicDataSource pool;

    public TicketDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public boolean add(Ticket ticket) {
        boolean rsl = false;
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_INTO_TICKET, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getSession().getId());
            ps.setInt(2, ticket.getPosRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUser().getId());
            rsl = ps.executeUpdate() > 0;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    ticket.setId(resultSet.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_TICKET)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    tickets.add(getTicket(resultSet));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return tickets;
    }

    private Ticket getTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getInt("id"),
                new Session(resultSet.getInt("session_id")),
                resultSet.getInt("pos_row"),
                resultSet.getInt("cell"),
                new User(resultSet.getInt("user_id"))
        );
    }
}
