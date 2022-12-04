package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@ThreadSafe
@Repository
public class JdbcUserRepository implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUserRepository.class.getName());
    private static final String INSERT_INTO_USER = "insert into users (name, email, phone) values (?, ?, ?)";
    private static final String SELECT_USERS_BY_EMAIL_AND_PHONE = "select * from users where email = ? and phone = ?";
    private final DataSource pool;

    public JdbcUserRepository(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public boolean add(User user) {
        boolean rsl = false;
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_INTO_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            rsl = ps.executeUpdate() > 0;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    @Override
    public Optional<User> findUserByEmailAndPhone(String email, String phone) {
        Optional<User> rsl = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_USERS_BY_EMAIL_AND_PHONE)
        ) {
            ps.setString(1, email);
            ps.setString(2, phone);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    rsl = Optional.of(getUser(it));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return rsl;
    }

    private User getUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"));
    }
}
