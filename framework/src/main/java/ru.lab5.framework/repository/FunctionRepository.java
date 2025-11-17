package ru.lab5.framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lab5.framework.model.FunctionEntity;

import java.util.List;

/**
 * Репозиторий для работы с табулированными функциями.
 */
public interface FunctionRepository extends JpaRepository<FunctionEntity, Long> {

    /**
     * Найти все функции, принадлежащие указанному пользователю.
     *
     * @param ownerId идентификатор владельца
     * @return список функций
     */
    List<FunctionEntity> findByOwner_Id(Long ownerId);

    /**
     * Поиск функций по фрагменту названия без учёта регистра.
     *
     * @param namePart часть названия
     * @return список подходящих функций
     */
    List<FunctionEntity> findByNameContainingIgnoreCase(String namePart);
}
