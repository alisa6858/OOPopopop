package ru.lab5.manual.dao;

import ru.lab5.manual.model.PermissionRecord;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс доступа к таблице permission.
 */
public interface PermissionDao {

    PermissionRecord insert(PermissionRecord permission);

    Optional<PermissionRecord> findById(long id);

    Optional<PermissionRecord> findByUserAndFunction(long userId, long functionId);

    List<PermissionRecord> findByUserId(long userId);

    PermissionRecord update(PermissionRecord permission);

    boolean deleteById(long id);

    void deleteAll();
}
