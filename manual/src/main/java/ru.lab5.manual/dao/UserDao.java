package ru.lab5.manual.dao;

import ru.lab5.manual.model.UserRecord;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс доступа к таблице app_user с помощью JDBC.
 */
public interface UserDao {

    UserRecord insert(UserRecord user);

    Optional<UserRecord> findById(long id);

    Optional<UserRecord> findByUsername(String username);

    List<UserRecord> findAll();

    UserRecord update(UserRecord user);

    boolean deleteById(long id);

    void deleteAll();
}
