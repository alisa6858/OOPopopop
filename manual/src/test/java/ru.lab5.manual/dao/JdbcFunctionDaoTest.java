package ru.lab5.manual.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.jdbc.JdbcFunctionDao;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.FunctionRecord;
import ru.lab5.manual.model.UserRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для JdbcFunctionDao.
 */
public class JdbcFunctionDaoTest {

    private final JdbcUserDao userDao = new JdbcUserDao();
    private final JdbcFunctionDao functionDao = new JdbcFunctionDao();
    private final Random random = new Random();

    @BeforeEach
    void cleanTables() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement st = conn.createStatement()) {
            // порядок важен из-за внешних ключей
            st.executeUpdate("DELETE FROM permission");
            st.executeUpdate("DELETE FROM point");
            st.executeUpdate("DELETE FROM app_function");
            st.executeUpdate("DELETE FROM app_user");
        }
    }

    @Test
    void fullCrudCycleForFunction() {
        // создаём пользователя-владельца
        UserRecord owner = new UserRecord("owner_" + random.nextInt(10_000),
                "pass_" + random.nextInt(10_000), 0);
        owner = userDao.insert(owner);

        // создаём функцию
        FunctionRecord f = new FunctionRecord(owner.getId(),
                "f(x)=" + random.nextInt(100),
                "тестовая функция");
        f = functionDao.insert(f);
        assertNotNull(f.getId());

        // читаем по id
        FunctionRecord fromDb = functionDao.findById(f.getId())
                .orElseThrow(() -> new AssertionError("Функция не найдена по id"));
        assertEquals(f.getName(), fromDb.getName());

        // обновляем
        f.setName("обновлённая_" + random.nextInt(1000));
        f.setDescription(null); // проверка работы с NULL
        functionDao.update(f);

        FunctionRecord updated = functionDao.findById(f.getId())
                .orElseThrow(() -> new AssertionError("Функция не найдена после обновления"));
        assertEquals(f.getName(), updated.getName());
        assertNull(updated.getDescription());

        // поиск по userId
        List<FunctionRecord> forUser = functionDao.findByUserId(owner.getId());
        assertEquals(1, forUser.size());

        // выбор всех
        List<FunctionRecord> all = functionDao.findAll();
        assertEquals(1, all.size());

        // удаление
        boolean deleted = functionDao.deleteById(f.getId());
        assertTrue(deleted);

        assertTrue(functionDao.findAll().isEmpty());
    }
}
