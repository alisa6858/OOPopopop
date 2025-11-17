package ru.lab5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lab5.framework.model.PointEntity;

import java.util.List;

/**
 * Репозиторий для работы с точками табулированной функции.
 */
public interface PointRepository extends JpaRepository<PointEntity, Long> {

    /**
     * Найти все точки указанной функции, отсортированные по индексу.
     *
     * @param functionId идентификатор функции
     * @return список точек
     */
    List<PointEntity> findByFunction_IdOrderByIndex(Long functionId);
}
