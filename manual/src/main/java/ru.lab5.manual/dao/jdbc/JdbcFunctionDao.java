package ru.lab5.manual.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab5.manual.dao.FunctionDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.FunctionRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC-реализация доступа к таблице app_function.
 */
public class JdbcFunctionDao implements FunctionDao {

    private static final Logger log = LoggerFactory.getLogger(JdbcFunctionDao.class);

    private static final String SQL_INSERT = """
            INSERT INTO app_function (user_id, name, description)
            VALUES (?, ?, ?)
            RETURNING function_id
            """;

    private static final String SQL_FIND_BY_ID = """
            SELECT function_id, user_id, name, description
            FROM app_function
            WHERE function_id = ?
            """;

    private static final String SQL_FIND_BY_USER_ID = """
            SELECT function_id, user_id, name, description
            FROM app_function
            WHERE user_id = ?
            ORDER BY name
            """;

    private static final String SQL_FIND_ALL = """
            SELECT function_id, user_id, name, description
            FROM app_function
            ORDER BY function_id
            """;

    private static final String SQL_UPDATE = """
            UPDATE app_function
            SET user_id = ?, name = ?, description = ?
            WHERE function_id = ?
            """;

    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM app_function
            WHERE function_id = ?
            """;

    private static final String SQL_DELETE_ALL = "DELETE FROM app_function";

    @Override
    public FunctionRecord insert(FunctionRecord function) {
        log.info("Вставка новой функции name={} userId={}", function.getName(), function.getUserId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setLong(1, function.getUserId());
            ps.setString(2, function.getName());
            if (function.getDescription() == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, function.getDescription());
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("function_id");
                    function.setId(id);
                    log.debug("Функция сохранена с id={}", id);
                    return function;
                }
                throw new SQLException("INSERT app_function вернул пустой результат");
            }
        } catch (SQLException e) {
            log.error("Ошибка при вставке функции", e);
            throw new RuntimeException("Ошибка при вставке функции", e);
        }
    }

    @Override
    public Optional<FunctionRecord> findById(long id) {
        log.debug("Поиск функции по id={}", id);
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
            log.error("Ошибка при поиске функции id={}", id, e);
            throw new RuntimeException("Ошибка при поиске функции", e);
        }
    }

    @Override
    public List<FunctionRecord> findByUserId(long userId) {
        log.debug("Поиск функций пользователя userId={}", userId);
        List<FunctionRecord> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
            log.info("Для пользователя {} найдено {} функций", userId, result.size());
            return result;
        } catch (SQLException e) {
            log.error("Ошибка при поиске функций по userId={}", userId, e);
            throw new RuntimeException("Ошибка при поиске функций", e);
        }
    }

    @Override
    public List<FunctionRecord> findAll() {
        log.debug("Загрузка всех функций");
        List<FunctionRecord> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
            log.info("Всего функций: {}", result.size());
            return result;
        } catch (SQLException e) {
            log.error("Ошибка при загрузке всех функций", e);
            throw new RuntimeException("Ошибка при загрузке функций", e);
        }
    }

    @Override
    public FunctionRecord update(FunctionRecord function) {
        if (function.getId() == null) {
            throw new IllegalArgumentException("Нельзя обновить функцию без id");
        }
        log.info("Обновление функции id={}", function.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setLong(1, function.getUserId());
            ps.setString(2, function.getName());
            if (function.getDescription() == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, function.getDescription());
            }
            ps.setLong(4, function.getId());

            int updated = ps.executeUpdate();
            if (updated != 1) {
                log.warn("Ожидалось обновить 1 строку, обновлено {}", updated);
            }
            return function;
        } catch (SQLException e) {
            log.error("Ошибка при обновлении функции id={}", function.getId(), e);
            throw new RuntimeException("Ошибка при обновлении функции", e);
        }
    }

    @Override
    public boolean deleteById(long id) {
        log.info("Удаление функции id={}", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_BY_ID)) {

            ps.setLong(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            log.error("Ошибка при удалении функции id={}", id, e);
            throw new RuntimeException("Ошибка при удалении функции", e);
        }
    }

    @Override
    public void deleteAll() {
        log.warn("Удаление всех функций");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_ALL)) {

            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при удалении всех функций", e);
            throw new RuntimeException("Ошибка при удалении функций", e);
        }
    }

    private FunctionRecord mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("function_id");
        long userId = rs.getLong("user_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        return new FunctionRecord(id, userId, name, description);
    }
}
