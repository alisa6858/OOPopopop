package ru.lab5.manual.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.jdbc.JdbcFunctionDao;
import ru.lab5.manual.dao.jdbc.JdbcPermissionDao;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.FunctionRecord;
import ru.lab5.manual.model.PermissionRecord;
import ru.lab5.manual.model.UserRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест для JdbcPermissionDao.
 */
public class JdbcPermissionDaoTest {

    private final JdbcUserDao userDao = new JdbcUserDao();
    private final JdbcFunctionDao functionDao = new JdbcFunctionDao();
    private final JdbcPermissionDao permissionDao = new JdbcPermissionDao();
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
    void fullCrudCycleForPermissions() {
        // владелец и функция
        UserRecord owner = userDao.insert(
                new UserRecord("owner_" + random.nextInt(10_000),
                        "pass_" + random.nextInt(10_000), 1)
        );
        FunctionRecord function = functionDao.insert(
                new FunctionRecord(owner.getId(), "g(x)", "функция для прав")
        );

        // другой пользователь, которому выдаём доступ
        UserRecord guest = userDao.insert(
                new UserRecord("guest_" + random.nextInt(10_000),
                        "pass_" + random.nextInt(10_000), 0)
        );

        // создаём разрешение
        PermissionRecord perm = new PermissionRecord(guest.getId(), function.getId(), 1);
        perm = permissionDao.insert(perm);
        assertNotNull(perm.getId());

        // поиск по id
        PermissionRecord fromDb = permissionDao.findById(perm.getId())
                .orElseThrow(() -> new AssertionError("Разрешение не найдено по id"));
        assertEquals(guest.getId(), fromDb.getUserId());

        // поиск по (user, function)
        PermissionRecord byPair = permissionDao
                .findByUserAndFunction(guest.getId(), function.getId())
                .orElseThrow(() -> new AssertionError("Разрешение не найдено по паре user/function"));
        assertEquals(perm.getId(), byPair.getId());

        // обновляем тип доступа
        perm.setAccess(2);
        permissionDao.update(perm);
        PermissionRecord updated = permissionDao.findById(perm.getId())
                .orElseThrow(() -> new AssertionError("Разрешение не найдено после обновления"));
        assertEquals(2, updated.getAccess());

        // добавляем второе разрешение для разнообразия
        UserRecord guest2 = userDao.insert(
                new UserRecord("guest2_" + random.nextInt(10_000),
                        "pass_" + random.nextInt(10_000), 0)
        );
        permissionDao.insert(new PermissionRecord(guest2.getId(), function.getId(), 1));

        // выбор всех разрешений пользователя
        List<PermissionRecord> permsForGuest = permissionDao.findByUserId(guest.getId());
        assertEquals(1, permsForGuest.size());

        // удаление по id
        boolean deleted = permissionDao.deleteById(perm.getId());
        assertTrue(deleted);

        // очистка всех
        permissionDao.deleteAll();
        List<PermissionRecord> empty = permissionDao.findByUserId(guest2.getId());
        assertTrue(empty.isEmpty());
    }
}
