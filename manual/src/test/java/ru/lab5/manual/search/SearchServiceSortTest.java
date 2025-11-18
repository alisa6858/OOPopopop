package ru.lab5.manual.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.FunctionDao;
import ru.lab5.manual.dao.PermissionDao;
import ru.lab5.manual.dao.PointDao;
import ru.lab5.manual.dao.UserDao;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.db.ConnectionFactory;
import ru.lab5.manual.dto.UserDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты корректности сортировки в SearchService (manual-вариант).
 *
 * В БД создаётся небольшой набор пользователей, после чего
 * проверяется порядок элементов при сортировке по имени
 * и по роли.
 */
class SearchServiceSortTest {

    private SearchService searchService;
    private JdbcUserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        userDao = new JdbcUserDao();

        // чистим таблицу пользователей перед каждым тестом
        try (Connection conn = ConnectionFactory.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM app_user");
        }

        // добавляем несколько пользователей вручную
        userDao.insert(new UserRecord(null, "alex", "p1", 0));
        userDao.insert(new UserRecord(null, "boris", "p2", 1));
        userDao.insert(new UserRecord(null, "anna", "p3", 0));
        userDao.insert(new UserRecord(null, "dmitry", "p4", 1));

        // остальные DAO здесь не нужны, делаем простые заглушки
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
    void testSortByUsernameAscending() {
        List<UserDto> users = searchService.searchUsers(
                null,
                null,
                SearchSortField.USERNAME,
                true
        );

        // ожидаем порядок по имени: alex, anna, boris, dmitry
        assertEquals("alex", users.get(0).getUsername());
        assertEquals("anna", users.get(1).getUsername());
        assertEquals("boris", users.get(2).getUsername());
        assertEquals("dmitry", users.get(3).getUsername());
    }

    @Test
    void testSortByUsernameDescending() {
        List<UserDto> users = searchService.searchUsers(
                null,
                null,
                SearchSortField.USERNAME,
                false
        );

        // обратный порядок
        assertEquals("dmitry", users.get(0).getUsername());
        assertEquals("boris", users.get(1).getUsername());
        assertEquals("anna", users.get(2).getUsername());
        assertEquals("alex", users.get(3).getUsername());
    }

    @Test
    void testSortByRoleThenUsername() {
        List<UserDto> users = searchService.searchUsers(
                null,
                null,
                SearchSortField.ROLE,
                true
        );

        // сначала роль 0 (в алфавитном порядке), потом роль 1
        assertEquals(0, users.get(0).getRole());
        assertEquals(0, users.get(1).getRole());
        assertEquals(1, users.get(2).getRole());
        assertEquals(1, users.get(3).getRole());

        assertEquals("alex", users.get(0).getUsername());
        assertEquals("anna", users.get(1).getUsername());
    }

    // --- простые заглушки для остальных DAO -----------------------------

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
