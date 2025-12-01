package ru.lab5.manual.perf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.model.UserRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Небольшой тест для измерения времени работы ручной (JDBC) реализации.
 * <p>
 * Сценарий:
 * 1) очистка таблиц;
 * 2) генерация 10 000 пользователей;
 * 3) вставка всех пользователей;
 * 4) чтение всех пользователей;
 * 5) удаление всех пользователей.
 * <p>
 * Время операций выводится в консоль в формате CSV:
 * manual;10000;insertMs;selectMs;deleteMs
 */
public class ManualUserDaoPerformanceTest {

    private final JdbcUserDao userDao = new JdbcUserDao();
    private final Random random = new Random();

    private static final int USERS_COUNT = 10_000;

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
    void measureManualUserDaoPerformance() {
        // 1. Генерация тестовых данных
        List<UserRecord> generated = new ArrayList<>(USERS_COUNT);
        for (int i = 0; i < USERS_COUNT; i++) {
            String username = "manual_user_" + i + "_" + random.nextInt(100_000);
            String password = "pass_" + random.nextInt(1_000_000);
            int role = random.nextBoolean() ? 0 : 1;
            generated.add(new UserRecord(username, password, role));
        }

        // 2. Вставка всех пользователей
        long tInsertStart = System.currentTimeMillis();
        for (UserRecord user : generated) {
            userDao.insert(user);
        }
        long tInsertEnd = System.currentTimeMillis();
        long insertMs = tInsertEnd - tInsertStart;

        // 3. Чтение всех пользователей
        long tSelectStart = System.currentTimeMillis();
        var allUsers = userDao.findAll();
        long tSelectEnd = System.currentTimeMillis();
        long selectMs = tSelectEnd - tSelectStart;

        assertEquals(USERS_COUNT, allUsers.size(),
                "Количество записей в app_user не совпадает с ожидаемым");

        // 4. Удаление всех пользователей
        long tDeleteStart = System.currentTimeMillis();
        userDao.deleteAll();
        long tDeleteEnd = System.currentTimeMillis();
        long deleteMs = tDeleteEnd - tDeleteStart;

        // 5. Вывод строки для таблички (можно потом скопировать в CSV)
        System.out.printf("manual;%d;%d;%d;%d%n",
                USERS_COUNT, insertMs, selectMs, deleteMs);
    }
}
