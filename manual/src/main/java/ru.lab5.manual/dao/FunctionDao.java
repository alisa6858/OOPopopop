package ru.lab5.manual.dao;

import ru.lab5.manual.model.FunctionRecord;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс доступа к таблице app_function.
 */
public interface FunctionDao {

    FunctionRecord insert(FunctionRecord function);

    Optional<FunctionRecord> findById(long id);

    List<FunctionRecord> findByUserId(long userId);

    List<FunctionRecord> findAll();

    FunctionRecord update(FunctionRecord function);

    boolean deleteById(long id);

    void deleteAll();
}
