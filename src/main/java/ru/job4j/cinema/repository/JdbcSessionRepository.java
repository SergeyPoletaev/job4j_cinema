package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
@Repository
public class JdbcSessionRepository implements SessionRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcSessionRepository.class.getName());
    private static final String SELECT_ALL_SESSIONS = "select * from sessions";
    private static final String INSERT_INTO_SESSION = "insert into sessions (name) values (?)";
    private final DataSource dataSource;

    public JdbcSessionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean add(Session session) {
        boolean rsl = false;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_INTO_SESSION, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, session.getName());
            rsl = ps.executeUpdate() > 0;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    session.setId(resultSet.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public List<Session> findAll() {
        List<Session> rsl = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SESSIONS)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    rsl.add(getSession(resultSet));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    private Session getSession(ResultSet resultSet) throws SQLException {
        return new Session(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
