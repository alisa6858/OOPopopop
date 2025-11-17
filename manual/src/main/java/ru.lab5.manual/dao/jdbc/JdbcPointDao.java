package ru.lab5.manual.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab5.manual.dao.PointDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.PointRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC-реализация доступа к таблице point.
 */
public class JdbcPointDao implements PointDao {

    private static final Logger log = LoggerFactory.getLogger(JdbcPointDao.class);

    private static final String SQL_INSERT = """
            INSERT INTO point (function_id, idx, x, y)
            VALUES (?, ?, ?, ?)
            RETURNING point_id
            """;

    private static final String SQL_FIND_BY_ID = """
            SELECT point_id, function_id, idx, x, y
            FROM point
            WHERE point_id = ?
            """;

    private static final String SQL_FIND_BY_FUNCTION_ID = """
            SELECT point_id, function_id, idx, x, y
            FROM point
            WHERE function_id = ?
            ORDER BY idx
            """;

    private static final String SQL_UPDATE = """
            UPDATE point
            SET function_id = ?, idx = ?, x = ?, y = ?
            WHERE point_id = ?
            """;

    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM point
            WHERE point_id = ?
            """;

    private static final String SQL_DELETE_BY_FUNCTION_ID = """
            DELETE FROM point
            WHERE function_id = ?
            """;

    @Override
    public PointRecord insert(PointRecord point) {
        log.info("Вставка точки functionId={} idx={}", point.getFunctionId(), point.getIndex());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setLong(1, point.getFunctionId());
            ps.setInt(2, point.getIndex());
            ps.setDouble(3, point.getX());
            ps.setDouble(4, point.getY());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("point_id");
                    point.setId(id);
                    log.debug("Точка сохранена с id={}", id);
                    return point;
                }
                throw new SQLException("INSERT point вернул пустой результат");
            }
        } catch (SQLException e) {
            log.error("Ошибка при вставке точки", e);
            throw new RuntimeException("Ошибка при вставке точки", e);
        }
    }

    @Override
    public Optional<PointRecord> findById(long id) {
        log.debug("Поиск точки по id={}", id);
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
            log.error("Ошибка при поиске точки id={}", id, e);
            throw new RuntimeException("Ошибка при поиске точки", e);
        }
    }

    @Override
    public List<PointRecord> findByFunctionId(long functionId) {
        log.debug("Поиск точек функции functionId={}", functionId);
        List<PointRecord> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_FUNCTION_ID)) {

            ps.setLong(1, functionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
            log.info("Для функции {} найдено {} точек", functionId, result.size());
            return result;
        } catch (SQLException e) {
            log.error("Ошибка при поиске точек functionId={}", functionId, e);
            throw new RuntimeException("Ошибка при поиске точек", e);
        }
    }

    @Override
    public PointRecord update(PointRecord point) {
        if (point.getId() == null) {
            throw new IllegalArgumentException("Нельзя обновить точку без id");
        }
        log.info("Обновление точки id={}", point.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setLong(1, point.getFunctionId());
            ps.setInt(2, point.getIndex());
            ps.setDouble(3, point.getX());
            ps.setDouble(4, point.getY());
            ps.setLong(5, point.getId());

            int updated = ps.executeUpdate();
            if (updated != 1) {
                log.warn("Ожидалось обновить 1 строку, обновлено {}", updated);
            }
            return point;
        } catch (SQLException e) {
            log.error("Ошибка при обновлении точки id={}", point.getId(), e);
            throw new RuntimeException("Ошибка при обновлении точки", e);
        }
    }

    @Override
    public boolean deleteById(long id) {
        log.info("Удаление точки id={}", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_BY_ID)) {

            ps.setLong(1, id);
            int deleted = ps.executeUpdate();
            return deleted > 0;
        } catch (SQLException e) {
            log.error("Ошибка при удалении точки id={}", id, e);
            throw new RuntimeException("Ошибка при удалении точки", e);
        }
    }

    @Override
    public void deleteAllByFunctionId(long functionId) {
        log.warn("Удаление всех точек функции functionId={}", functionId);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_BY_FUNCTION_ID)) {

            ps.setLong(1, functionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Ошибка при удалении точек функции functionId={}", functionId, e);
            throw new RuntimeException("Ошибка при удалении точек функции", e);
        }
    }

    private PointRecord mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("point_id");
        long functionId = rs.getLong("function_id");
        int idx = rs.getInt("idx");
        double x = rs.getDouble("x");
        double y = rs.getDouble("y");
        return new PointRecord(id, functionId, idx, x, y);
    }
}
