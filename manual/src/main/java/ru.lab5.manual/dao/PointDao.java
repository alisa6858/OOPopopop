package ru.lab5.manual.dao;

import ru.lab5.manual.model.PointRecord;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс доступа к таблице point.
 */
public interface PointDao {

    PointRecord insert(PointRecord point);

    Optional<PointRecord> findById(long id);

    List<PointRecord> findByFunctionId(long functionId);

    PointRecord update(PointRecord point);

    boolean deleteById(long id);

    void deleteAllByFunctionId(long functionId);
}
