package ru.lab5.manual.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.FunctionDao;
import ru.lab5.manual.dao.PermissionDao;
import ru.lab5.manual.dao.PointDao;
import ru.lab5.manual.dao.UserDao;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.dto.mapper.RecordToDtoMapper;
import ru.lab5.manual.model.FunctionRecord;
import ru.lab5.manual.model.PermissionRecord;
import ru.lab5.manual.model.PointRecord;
import ru.lab5.manual.model.UserRecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Небольшой "бенчмарк" сортировки для manual-варианта.
 * <p>
 * Генерируется USERS_COUNT записей, измеряется время вызова
 * searchUsers c сортировкой по имени и по роли.
 * Результат выводится в виде CSV-строки:
 * manual_sort;USERS_COUNT;username_ms;role_ms
 */
class SearchServiceSortPerformanceTest {

    private static final int USERS_COUNT = 10_000;

    private SearchService searchService;
    private JdbcUserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        userDao = new JdbcUserDao();

        try (Connection conn = ConnectionFactory.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM app_user");
        }

        // генерим пользователей
        Random random = new Random();
        for (int i = 0; i < USERS_COUNT; i++) {
            String username = "user_" + i + "_" + random.nextInt(100_000);
            String password = "p_" + random.nextInt(1_000_000);
            int role = random.nextBoolean() ? 0 : 1;
            userDao.insert(new UserRecord(null, username, password, role));
        }

        FunctionDao functionDao = new EmptyFunctionDao();
        PointDao pointDao = new EmptyPointDao();
        PermissionDao permissionDao = new EmptyPermissionDao();

        searchService = new SearchService(
                userDao,
                functionDao,
                pointDao,
                permissionDao,
                new RecordToDtoMapper()
        );
    }

    @Test
    void measureSortPerformance() {
        long t1 = System.currentTimeMillis();
        searchService.searchUsers(null, null, SearchSortField.USERNAME, true);
        long t2 = System.currentTimeMillis();
        long usernameMs = t2 - t1;

        long t3 = System.currentTimeMillis();
        searchService.searchUsers(null, null, SearchSortField.ROLE, true);
        long t4 = System.currentTimeMillis();
        long roleMs = t4 - t3;

        System.out.printf("manual_sort;%d;%d;%d%n",
                USERS_COUNT, usernameMs, roleMs);
    }

    // те же пустые DAO, что и в предыдущем тесте

    private static class EmptyFunctionDao implements FunctionDao {
        @Override
        public FunctionRecord insert(FunctionRecord function) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<FunctionRecord> findById(long id) {
            return Optional.empty();
        }

        @Override
        public List<FunctionRecord> findByUserId(long userId) {
            return List.of();
        }

        @Override
        public List<FunctionRecord> findAll() {
            return List.of();
        }

        @Override
        public FunctionRecord update(FunctionRecord function) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean deleteById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }
    }

    private static class EmptyPointDao implements PointDao {
        @Override
        public PointRecord insert(PointRecord point) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<PointRecord> findById(long id) {
            return Optional.empty();
        }

        @Override
        public List<PointRecord> findByFunctionId(long functionId) {
            return List.of();
        }

        @Override
        public PointRecord update(PointRecord point) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean deleteById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllByFunctionId(long functionId) {
            throw new UnsupportedOperationException();
        }
    }

    private static class EmptyPermissionDao implements PermissionDao {
        @Override
        public PermissionRecord insert(PermissionRecord permission) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<PermissionRecord> findById(long id) {
            return Optional.empty();
        }

        @Override
        public Optional<PermissionRecord> findByUserAndFunction(long userId, long functionId) {
            return Optional.empty();
        }

        @Override
        public List<PermissionRecord> findByUserId(long userId) {
            return List.of();
        }

        @Override
        public PermissionRecord update(PermissionRecord permission) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean deleteById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }
    }
}
