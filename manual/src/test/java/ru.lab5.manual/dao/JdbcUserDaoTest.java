package ru.lab5.manual.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.UserRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для {@link JdbcUserDao}.
 * <p>
 * Перед запуском теста база данных должна быть создана,
 * а таблицы — созданы с помощью скрипта 01_create_tables.sql.
 */
public class JdbcUserDaoTest {

    private final JdbcUserDao userDao = new JdbcUserDao();
    private final Random random = new Random();

    @BeforeEach
    void cleanTable() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM app_user");
        }
    }

    @Test
    void fullCrudCycle() {
        // генерация и добавление пользователей
        UserRecord u1 = randomUser("ivan");
        UserRecord u2 = randomUser("petr");
        UserRecord u3 = randomUser("sergey");

        u1 = userDao.insert(u1);
        u2 = userDao.insert(u2);
        u3 = userDao.insert(u3);

        assertNotNull(u1.getId());
        assertNotNull(u2.getId());
        assertNotNull(u3.getId());

        // поиск по id
        Optional<UserRecord> fromDb = userDao.findById(u1.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(u1.getUsername(), fromDb.get().getUsername());

        // поиск по username
        Optional<UserRecord> byName = userDao.findByUsername("petr");
        assertTrue(byName.isPresent());
        assertEquals(u2.getId(), byName.get().getId());

        // обновление
        u3.setPassword("new_pass_" + random.nextInt(1000));
        userDao.update(u3);
        Optional<UserRecord> updated = userDao.findById(u3.getId());
        assertTrue(updated.isPresent());
        assertEquals(u3.getPassword(), updated.get().getPassword());

        // выбор всех
        List<UserRecord> all = userDao.findAll();
        assertEquals(3, all.size());

        // удаление
        boolean deleted = userDao.deleteById(u2.getId());
        assertTrue(deleted);

        List<UserRecord> afterDelete = userDao.findAll();
        assertEquals(2, afterDelete.size());
    }

    private UserRecord randomUser(String baseName) {
        int role = random.nextBoolean() ? 0 : 1;
        String username = baseName + "_" + random.nextInt(10_000);
        String password = "pass_" + random.nextInt(10_000);
        return new UserRecord(username, password, role);
    }
}
