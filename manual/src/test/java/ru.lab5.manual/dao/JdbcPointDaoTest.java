package ru.lab5.manual.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.jdbc.JdbcFunctionDao;
import ru.lab5.manual.dao.jdbc.JdbcPointDao;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.FunctionRecord;
import ru.lab5.manual.model.PointRecord;
import ru.lab5.manual.model.UserRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для JdbcPointDao.
 */
public class JdbcPointDaoTest {

    private final JdbcUserDao userDao = new JdbcUserDao();
    private final JdbcFunctionDao functionDao = new JdbcFunctionDao();
    private final JdbcPointDao pointDao = new JdbcPointDao();
    private final Random random = new Random();

    @BeforeEach
    void cleanTables() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM permission");
            st.executeUpdate("DELETE FROM point");
            st.executeUpdate("DELETE FROM app_function");
            st.executeUpdate("DELETE FROM app_user");
        }
    }

    @Test
    void fullCrudCycleForPoints() {
        // владелец и функция
        UserRecord owner = userDao.insert(
                new UserRecord("user_" + random.nextInt(10_000),
                        "pass_" + random.nextInt(10_000), 0)
        );
        FunctionRecord f = functionDao.insert(
                new FunctionRecord(owner.getId(), "f_test", "для точек")
        );

        // вставляем несколько точек
        PointRecord p1 = pointDao.insert(new PointRecord(f.getId(), 0, 0.0, 1.0));
        PointRecord p2 = pointDao.insert(new PointRecord(f.getId(), 1, 1.0, 2.0));
        PointRecord p3 = pointDao.insert(new PointRecord(f.getId(), 2, 2.0, 4.0));

        assertNotNull(p1.getId());
        assertNotNull(p2.getId());
        assertNotNull(p3.getId());

        // поиск по id
        PointRecord fromDb = pointDao.findById(p2.getId())
                .orElseThrow(() -> new AssertionError("Точка не найдена по id"));
        assertEquals(1, fromDb.getIndex());
        assertEquals(1.0, fromDb.getX(), 1e-9);

        // обновление
        p3.setY(8.0);
        pointDao.update(p3);
        PointRecord updated = pointDao.findById(p3.getId())
                .orElseThrow(() -> new AssertionError("Точка не найдена после обновления"));
        assertEquals(8.0, updated.getY(), 1e-9);

        // поиск по functionId
        List<PointRecord> points = pointDao.findByFunctionId(f.getId());
        assertEquals(3, points.size());

        // удаление одной точки
        boolean deleted = pointDao.deleteById(p1.getId());
        assertTrue(deleted);

        List<PointRecord> afterDeleteOne = pointDao.findByFunctionId(f.getId());
        assertEquals(2, afterDeleteOne.size());

        // удаление всех точек функции
        pointDao.deleteAllByFunctionId(f.getId());
        List<PointRecord> empty = pointDao.findByFunctionId(f.getId());
        assertTrue(empty.isEmpty());
    }
}
