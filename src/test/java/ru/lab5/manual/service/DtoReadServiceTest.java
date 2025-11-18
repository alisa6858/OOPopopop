package ru.lab5.manual.service;

import org.junit.jupiter.api.Test;
import ru.lab5.manual.dao.FunctionDao;
import ru.lab5.manual.dao.PermissionDao;
import ru.lab5.manual.dao.PointDao;
import ru.lab5.manual.dao.UserDao;
import ru.lab5.manual.dto.FunctionDto;
import ru.lab5.manual.dto.PermissionDto;
import ru.lab5.manual.dto.PointDto;
import ru.lab5.manual.dto.UserDto;
import ru.lab5.manual.dto.mapper.RecordToDtoMapper;
import ru.lab5.manual.model.FunctionRecord;
import ru.lab5.manual.model.PermissionRecord;
import ru.lab5.manual.model.PointRecord;
import ru.lab5.manual.model.UserRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест сервиса DtoReadService с использованием
 * простых "фейковых" реализаций DAO без подключения к БД.
 */
class DtoReadServiceTest {

    @Test
    void testLoadAllUsers() {
        FakeUserDao userDao = new FakeUserDao();
        FakeFunctionDao functionDao = new FakeFunctionDao();
        FakePointDao pointDao = new FakePointDao();
        FakePermissionDao permissionDao = new FakePermissionDao();

        DtoReadService service = new DtoReadService(
                userDao, functionDao, pointDao, permissionDao, new RecordToDtoMapper()
        );

        List<UserDto> users = service.loadAllUsers();
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    void testLoadPointsForFunction() {
        FakeUserDao userDao = new FakeUserDao();
        FakeFunctionDao functionDao = new FakeFunctionDao();
        FakePointDao pointDao = new FakePointDao();
        FakePermissionDao permissionDao = new FakePermissionDao();

        DtoReadService service = new DtoReadService(
                userDao, functionDao, pointDao, permissionDao, new RecordToDtoMapper()
        );

        List<PointDto> points = service.loadPointsForFunction(100L);
        assertEquals(3, points.size());
        assertEquals(0, points.get(0).getIndex());
        assertEquals(2, points.get(2).getIndex());
    }

    @Test
    void testLoadPermissionsForUser() {
        FakeUserDao userDao = new FakeUserDao();
        FakeFunctionDao functionDao = new FakeFunctionDao();
        FakePointDao pointDao = new FakePointDao();
        FakePermissionDao permissionDao = new FakePermissionDao();

        DtoReadService service = new DtoReadService(
                userDao, functionDao, pointDao, permissionDao, new RecordToDtoMapper()
        );

        List<PermissionDto> perms = service.loadPermissionsForUser(1L);
        assertEquals(1, perms.size());
        assertEquals(1L, perms.get(0).getUserId());
        assertEquals(200L, perms.get(0).getFunctionId());
    }

    /**
     * Простая внутренняя реализация UserDao для теста.
     * Работает только на чтение, возвращая заранее подготовленный список.
     */
    private static class FakeUserDao implements UserDao {

        @Override
        public UserRecord insert(UserRecord user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<UserRecord> findById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<UserRecord> findByUsername(String username) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<UserRecord> findAll() {
            List<UserRecord> result = new ArrayList<>();
            result.add(new UserRecord(1L, "user1", "p1", 0));
            result.add(new UserRecord(2L, "user2", "p2", 1));
            return result;
        }

        @Override
        public UserRecord update(UserRecord user) {
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

    private static class FakeFunctionDao implements FunctionDao {

        @Override
        public FunctionRecord insert(FunctionRecord function) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<FunctionRecord> findById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<FunctionRecord> findByUserId(long userId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<FunctionRecord> findAll() {
            List<FunctionRecord> result = new ArrayList<>();
            result.add(new FunctionRecord(10L, 1L, "f1", "desc1"));
            result.add(new FunctionRecord(11L, 1L, "f2", "desc2"));
            return result;
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

    private static class FakePointDao implements PointDao {

        @Override
        public PointRecord insert(PointRecord point) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<PointRecord> findById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<PointRecord> findByFunctionId(long functionId) {
            List<PointRecord> result = new ArrayList<>();
            result.add(new PointRecord(1L, functionId, 0, 0.0, 0.0));
            result.add(new PointRecord(2L, functionId, 1, 1.0, 1.0));
            result.add(new PointRecord(3L, functionId, 2, 2.0, 4.0));
            return result;
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

    private static class FakePermissionDao implements PermissionDao {

        @Override
        public PermissionRecord insert(PermissionRecord permission) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<PermissionRecord> findById(long id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<PermissionRecord> findByUserAndFunction(long userId, long functionId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<PermissionRecord> findByUserId(long userId) {
            List<PermissionRecord> result = new ArrayList<>();
            if (userId == 1L) {
                result.add(new PermissionRecord(5L, 1L, 200L, 1));
            }
            return result;
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
