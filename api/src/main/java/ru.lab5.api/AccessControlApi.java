package ru.lab5.api;

import java.util.List;
import java.util.Optional;

/**
 * Общий контракт API для работы с данными.
 *
 * Этот интерфейс не зависит от способа реализации.
 * Ожидается, что:
 *  - manual-реализация будет работать через JDBC и собственные DAO;
 *  - framework-реализация будет работать через Spring Data / Hibernate.
 *
 * В лабораторной работе №7 по этому интерфейсу можно строить веб-API или клиент.
 */
public interface AccessControlApi {

    // -------- Пользователи --------

    /**
     * Создаёт нового пользователя.
     *
     * @param user данные пользователя без идентификатора
     * @return созданный пользователь с заполненным id
     */
    UserDto createUser(UserDto user);

    /**
     * Ищет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь или пустой Optional, если пользователь не найден
     */
    Optional<UserDto> getUserById(Long id);

    /**
     * Возвращает список пользователей с учётом фильтрации, сортировки и пагинации.
     *
     * @param request параметры поиска и сортировки
     * @return список пользователей, удовлетворяющих условиям
     */
    List<UserDto> findUsers(UserSearchRequest request);

    /**
     * Обновляет данные пользователя.
     *
     * @param id   идентификатор пользователя
     * @param user новые данные пользователя (без изменения id)
     * @return обновлённый пользователь или пустой Optional, если id не найден
     */
    Optional<UserDto> updateUser(Long id, UserDto user);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return true, если пользователь был удалён, false, если пользователь не найден
     */
    boolean deleteUser(Long id);

    // -------- Функции --------

    /**
     * Создаёт новую функцию.
     */
    FunctionDto createFunction(FunctionDto function);

    /**
     * Возвращает функцию по идентификатору.
     */
    Optional<FunctionDto> getFunctionById(Long id);

    /**
     * Возвращает все функции без фильтрации.
     */
    List<FunctionDto> getAllFunctions();

    /**
     * Обновляет данные функции.
     */
    Optional<FunctionDto> updateFunction(Long id, FunctionDto function);

    /**
     * Удаляет функцию по идентификатору.
     */
    boolean deleteFunction(Long id);

    // -------- Точки --------

    /**
     * Создаёт новую точку внутри функции.
     */
    PointDto createPoint(PointDto point);

    /**
     * Ищет точку по идентификатору.
     */
    Optional<PointDto> getPointById(Long id);

    /**
     * Возвращает все точки по идентификатору функции.
     */
    List<PointDto> getPointsByFunctionId(Long functionId);

    /**
     * Обновляет данные точки.
     */
    Optional<PointDto> updatePoint(Long id, PointDto point);

    /**
     * Удаляет точку по идентификатору.
     */
    boolean deletePoint(Long id);

    // -------- Права доступа --------

    /**
     * Выдаёт право доступа пользователя к функции.
     */
    PermissionDto grantPermission(PermissionDto permission);

    /**
     * Удаляет право доступа по идентификатору.
     */
    boolean revokePermission(Long permissionId);

    /**
     * Возвращает список всех прав пользователя.
     */
    List<PermissionDto> getPermissionsByUserId(Long userId);

    // -------- Поиск по структуре (задание 5-й лабы) --------

    /**
     * Глубинный обход структуры функций.
     * В manual-ветке это можно реализовать через поиск в глубину.
     *
     * @param rootFunctionId идентификатор корневой функции
     * @return список функций в порядке глубинного обхода
     */
    List<FunctionDto> traverseFunctionsDepthFirst(Long rootFunctionId);

    /**
     * Обход структуры функций в ширину.
     *
     * @param rootFunctionId идентификатор корневой функции
     * @return список функций в порядке обхода в ширину
     */
    List<FunctionDto> traverseFunctionsBreadthFirst(Long rootFunctionId);

    /**
     * Возвращает иерархию функций.
     * В простом варианте можно вернуть функции, связанные с корнем.
     *
     * @param rootFunctionId идентификатор корневой функции
     * @return список функций, связанных с корнем
     */
    List<FunctionDto> loadFunctionHierarchy(Long rootFunctionId);
}
