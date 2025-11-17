package ru.lab5.manual.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab5.manual.dao.PermissionDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.PermissionRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC-реализация доступа к таблице permission.
 */
public class JdbcPermissionDao implements PermissionDao {

    private static final Logger log = LoggerFactory.getLogger(JdbcPermissionDao.class);

    private static final String SQL_INSERT = """
            INSERT INTO permission (user_id, function_id, access)
            VALUES (?, ?, ?)
            RETURNING permission_id
            """;

    private static final String SQL_FIND_BY_ID = """
            SELECT permission_id, user_id, function_id, access
            FROM permission
            WHERE permission_id = ?
            """;

    private static final String SQL_FIND_BY_USER_AND_FUNCTION = """
            SELECT permission_id, user_id, function_id, access
            FROM permission
            WHERE user_id = ? AND function_id = ?
            """;

    private static final String SQL_FIND_BY_USER_ID = """
            SELECT permission_id, user_id, function_id, access
            FROM permission
            WHERE user_id = ?
            ORDER BY function_id
            """;

    private static final String SQL_UPDATE = """
            UPDATE permission
            SET user_id = ?, function_id = ?, access = ?
            WHERE permission_id = ?
            """;

    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM permission
            WHERE permission_id = ?
            """;

    private static final String SQL_DELETE_ALL = "DELETE FROM permission";

    @Override
    public PermissionRecord insert(PermissionRecord permission) {
        log.info("Вставка разрешения userId={} functionId={} access={}",
                permission.getUserId(), permission.getFunctionId(), permission.getAccess());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setLong(1, permission.getUserId());
            ps.setLong(2, permission.getFunctionId());
            ps.setInt(3, permission.getAccess());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("permission_id");
                    permission.setId(id);
                    log.debug("Разрешение сохранено с id={}", id);
                    return permission;
                }
                throw new SQLException("INSERT permission вернул пустой результат");
            }
        } catch (SQLException e) {
            log.error("Ошибка при вставке разрешения", e);
            throw new RuntimeException("Ошибка при вставке разрешения", e);
        }
    }

    @Override
    public Optional<PermissionRecord> findById(long id) {
        log.debug("Поиск разрешения по id={}", id);
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
            log.error("Ошибка при поиске разрешения id={}", id, e);
            throw new RuntimeException("Ошибка при поиске разрешения", e);
        }
    }

    @Override
    public Optional<PermissionRecord> findByUserAndFunction(long userId, long functionId) {
        log.debug("Поиск разрешения userId={} functionId={}", userId, functionId);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_AND_FUNCTION)) {

            ps.setLong(1, userId);
            ps.setLong(2, functionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Ошибка при поиске разрешения userId={} functionId={}", userId, functionId, e);
            throw new RuntimeException("Ошибка при поиске разрешения", e);
        }
    }

    @Override
    public List<PermissionRecord> findByUserId(long userId) {
        log.debug("Поиск всех разрешений пользователя userId={}", userId);
        List<PermissionRecord> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USER_ID)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
            log.info("Для пользователя {} найдено {} разрешений", userId, result.size());
            return result;
        } catch (SQLException e) {
            log.error("Ошибка при поиске разрешений по userId={}", userId, e);
            throw new RuntimeException("Ошибка при поиске разрешений", e);
        }
    }

    @Override
    public PermissionRecord update(PermissionRecord permission) {
        if (permission.getId() == null) {
            throw new IllegalArgumentException("Нельзя обновить разрешение без id");
        }
        log.info("Обновление разрешения id={}", permission.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setLong(1, permission.getUserId());
            ps.setLong(2, permission.getFunctionId());
            ps.setInt(3, permission.getAccess());
            ps.setLong(4, permission.getId());

            int updated = ps.executeUpdate();
            if (updated != 1) {
                log.warn("Ожидалось обновить 1 строку, обновлено {}", updated);
            }
            return permission;
        } catch (SQLException e) {
            log.error("Ошибка при обновлении разрешения id={}", permission.getId(), e);
            throw new RuntimeException("Ошибка при обновлении разрешения", e);
        }
    }

    @Override
    public boolean deleteById(long id) {
        log.info("Удаление разрешения id={}", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_BY_ID)) {

            ps.setLong(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            log.error("Ошибка при удалении разрешения id={}", id, e);
            throw new RuntimeException("Ошибка при удалении разрешения", e);
        }
    }

    @Override
    public void deleteAll() {
        log.warn("Удаление всех разрешений");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_ALL)) {

            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при удалении всех разрешений", e);
            throw new RuntimeException("Ошибка при удалении разрешений", e);
        }
    }

    private PermissionRecord mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("permission_id");
        long userId = rs.getLong("user_id");
        long functionId = rs.getLong("function_id");
        int access = rs.getInt("access");
        return new PermissionRecord(id, userId, functionId, access);
    }
}
