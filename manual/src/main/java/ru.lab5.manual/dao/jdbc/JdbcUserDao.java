package ru.lab5.manual.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab5.manual.dao.UserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.UserRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация {@link UserDao} на чистом JDBC.
 * <p>
 * Использует запросы, эквивалентные записанным в
 * файле 02_crud_queries.sql.
 */
public class JdbcUserDao implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);

    private static final String SQL_INSERT = """
            INSERT INTO app_user (username, password, role)
            VALUES (?, ?, ?)
            RETURNING user_id
            """;

    private static final String SQL_FIND_BY_ID = """
            SELECT user_id, username, password, role
            FROM app_user
            WHERE user_id = ?
            """;

    private static final String SQL_FIND_BY_USERNAME = """
            SELECT user_id, username, password, role
            FROM app_user
            WHERE username = ?
            """;

    private static final String SQL_FIND_ALL = """
            SELECT user_id, username, password, role
            FROM app_user
            ORDER BY user_id
            """;

    private static final String SQL_UPDATE = """
            UPDATE app_user
            SET username = ?, password = ?, role = ?
            WHERE user_id = ?
            """;

    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM app_user
            WHERE user_id = ?
            """;

    private static final String SQL_DELETE_ALL = "DELETE FROM app_user";

    @Override
    public UserRecord insert(UserRecord user) {
        log.info("Вставка нового пользователя: username={}", user.getUsername());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRole());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("user_id");
                    user.setId(id);
                    log.debug("Пользователь сохранён с id={}", id);
                    return user;
                } else {
                    throw new SQLException("INSERT app_user вернул пустой результат");
                }
            }
        } catch (SQLException e) {
            log.error("Ошибка при вставке пользователя", e);
            throw new RuntimeException("Ошибка при вставке пользователя", e);
        }
    }

    @Override
    public Optional<UserRecord> findById(long id) {
        log.debug("Поиск пользователя по id={}", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске пользователя по id={}", id, e);
            throw new RuntimeException("Ошибка при поиске пользователя", e);
        }
    }

    @Override
    public Optional<UserRecord> findByUsername(String username) {
        log.debug("Поиск пользователя по username={}", username);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USERNAME)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске пользователя по username={}", username, e);
            throw new RuntimeException("Ошибка при поиске пользователя", e);
        }
    }

    @Override
    public List<UserRecord> findAll() {
        log.debug("Загрузка всех пользователей");
        List<UserRecord> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
            log.info("Найдено {} пользователей", result.size());
            return result;
        } catch (SQLException e) {
            log.error("Ошибка при загрузке всех пользователей", e);
            throw new RuntimeException("Ошибка при загрузке пользователей", e);
        }
    }

    @Override
    public UserRecord update(UserRecord user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Нельзя обновить пользователя без id");
        }
        log.info("Обновление пользователя id={}", user.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRole());
            ps.setLong(4, user.getId());

            int updated = ps.executeUpdate();
            if (updated != 1) {
                log.warn("Ожидалось обновить 1 строку, обновлено {}", updated);
            }
            return user;
        } catch (SQLException e) {
            log.error("Ошибка при обновлении пользователя id={}", user.getId(), e);
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public boolean deleteById(long id) {
        log.info("Удаление пользователя id={}", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_BY_ID)) {

            ps.setLong(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            log.error("Ошибка при удалении пользователя id={}", id, e);
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    @Override
    public void deleteAll() {
        log.warn("Удаление всех пользователей");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_ALL)) {

            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при удалении всех пользователей", e);
            throw new RuntimeException("Ошибка при удалении пользователей", e);
        }
    }

    private UserRecord mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        int role = rs.getInt("role");
        return new UserRecord(id, username, password, role);
    }
}
