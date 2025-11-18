package ru.lab5.framework.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.lab5.framework.model.FunctionEntity;
import ru.lab5.framework.model.PermissionEntity;
import ru.lab5.framework.model.PointEntity;
import ru.lab5.framework.model.UserEntity;
import ru.lab5.framework.repository.FunctionRepository;
import ru.lab5.framework.repository.PermissionRepository;
import ru.lab5.framework.repository.PointRepository;
import ru.lab5.framework.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис поиска данных в варианте с Spring Data JPA.
 * <p>
 * Использует репозитории для доступа к сущностям и реализует:
 * <ul>
 *     <li>одиночный поиск пользователя по логину;</li>
 *     <li>множественный поиск пользователей с фильтрами и сортировкой;</li>
 *     <li>поиск в ширину по иерархии
 *          Пользователь → Функции → Точки;</li>
 *     <li>поиск в глубину по этой же иерархии;</li>
 *     <li>иерархический поиск точек по диапазону X;</li>
 *     <li>получение прав доступа пользователя.</li>
 * </ul>
 */
@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final UserRepository userRepository;
    private final FunctionRepository functionRepository;
    private final PointRepository pointRepository;
    private final PermissionRepository permissionRepository;

    public SearchService(UserRepository userRepository,
                         FunctionRepository functionRepository,
                         PointRepository pointRepository,
                         PermissionRepository permissionRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository");
        this.functionRepository = Objects.requireNonNull(functionRepository, "functionRepository");
        this.pointRepository = Objects.requireNonNull(pointRepository, "pointRepository");
        this.permissionRepository = Objects.requireNonNull(permissionRepository, "permissionRepository");
    }

    // ---------------------------------------------------------------------
    // ОДИНОЧНЫЙ ПОИСК
    // ---------------------------------------------------------------------

    /**
     * Одиночный поиск пользователя по точному логину.
     *
     * @param username логин пользователя
     * @return список из одного пользователя или пустой список,
     * если пользователь не найден.
     */
    public List<UserEntity> findUserByUsername(String username) {
        log.info("Одиночный поиск пользователя по логину: {}", username);
        if (username == null || username.isBlank()) {
            log.warn("Пустой логин в одиночном поиске");
            return List.of();
        }

        return userRepository.findByUsername(username.trim())
                .map(user -> {
                    log.debug("Найден пользователь id={} username={}", user.getId(), user.getUsername());
                    return List.of(user);
                })
                .orElseGet(() -> {
                    log.info("Пользователь с логином {} не найден", username);
                    return List.of();
                });
    }

    // ---------------------------------------------------------------------
    // МНОЖЕСТВЕННЫЙ ПОИСК С ФИЛЬТРАМИ И СОРТИРОВКОЙ
    // ---------------------------------------------------------------------

    /**
     * Множественный поиск пользователей с фильтрами и сортировкой.
     *
     * @param usernamePart часть логина пользователя (может быть null)
     * @param role         роль (может быть null, тогда роль не учитывается)
     * @param sortField    поле сортировки
     * @param ascending    направление сортировки:
     *                     {@code true} — по возрастанию,
     *                     {@code false} — по убыванию
     * @return отсортированный список подходящих пользователей
     */
    public List<UserEntity> searchUsers(String usernamePart,
                                        Integer role,
                                        SearchSortField sortField,
                                        boolean ascending) {
        log.info("Множественный поиск пользователей. fragment='{}', role={}, sortField={}, ascending={}",
                usernamePart, role, sortField, ascending);

        List<UserEntity> all = userRepository.findAll();
        log.debug("Всего пользователей в базе: {}", all.size());

        var stream = all.stream();

        if (usernamePart != null && !usernamePart.isBlank()) {
            String pattern = usernamePart.toLowerCase();
            stream = stream.filter(u -> u.getUsername() != null
                    && u.getUsername().toLowerCase().contains(pattern));
        }

        if (role != null) {
            stream = stream.filter(u -> Objects.equals(u.getRole(), role));
        }

        Comparator<UserEntity> comparator;
        if (sortField == SearchSortField.ROLE) {
            comparator = Comparator.comparing(UserEntity::getRole, Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(UserEntity::getUsername, Comparator.nullsLast(String::compareToIgnoreCase));
        } else {
            comparator = Comparator.comparing(UserEntity::getUsername,
                    Comparator.nullsLast(String::compareToIgnoreCase));
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        List<UserEntity> result = stream
                .sorted(comparator)
                .collect(Collectors.toList());

        log.debug("После фильтрации и сортировки осталось {} пользователей", result.size());
        return result;
    }

    // ---------------------------------------------------------------------
    // ПОИСК В ШИРИНУ
    // ---------------------------------------------------------------------

    /**
     * Поиск в ширину по дереву:
     * Пользователь → Функции → Точки.
     * <p>
     * Для заданного пользователя берутся его функции, далее по очереди
     * обходятся все функции и собираются точки.
     *
     * @param userId идентификатор пользователя
     * @return список точек в порядке обхода в ширину
     */
    public List<PointEntity> breadthFirstPoints(long userId) {
        log.info("Поиск в ширину для точек пользователя id={}", userId);

        List<PointEntity> result = new ArrayList<>();

        List<FunctionEntity> functions = functionRepository.findByOwner_Id(userId);
        log.debug("Для пользователя id={} найдено {} функций", userId, functions.size());

        Deque<FunctionEntity> queue = new ArrayDeque<>(functions);

        while (!queue.isEmpty()) {
            FunctionEntity current = queue.removeFirst();
            log.debug("Обработка функции id={} name={}", current.getId(), current.getName());

            List<PointEntity> points = pointRepository
                    .findByFunction_IdOrderByIndex(current.getId());
            log.debug("Для функции id={} найдено {} точек", current.getId(), points.size());

            result.addAll(points);
        }

        log.info("Поиск в ширину завершён, всего точек: {}", result.size());
        return result;
    }

    // ---------------------------------------------------------------------
    // ПОИСК В ГЛУБИНУ
    // ---------------------------------------------------------------------

    /**
     * Поиск в глубину по дереву:
     * Пользователь → Функции → Точки.
     *
     * @param userId идентификатор пользователя
     * @return список точек в порядке обхода в глубину
     */
    public List<PointEntity> depthFirstPoints(long userId) {
        log.info("Поиск в глубину для точек пользователя id={}", userId);

        List<PointEntity> result = new ArrayList<>();
        List<FunctionEntity> functions = functionRepository.findByOwner_Id(userId);

        log.debug("Для пользователя id={} найдено {} функций", userId, functions.size());

        for (FunctionEntity function : functions) {
            dfsForFunction(function, result);
        }

        log.info("Поиск в глубину завершён, всего точек: {}", result.size());
        return result;
    }

    /**
     * Вспомогательный метод DFS для одной функции.
     */
    private void dfsForFunction(FunctionEntity function,
                                List<PointEntity> accumulator) {
        log.debug("DFS: функция id={} name={}", function.getId(), function.getName());

        List<PointEntity> points =
                pointRepository.findByFunction_IdOrderByIndex(function.getId());

        for (PointEntity point : points) {
            accumulator.add(point);
            log.trace("DFS: добавлена точка id={} index={} x={} y={}",
                    point.getId(), point.getIndex(), point.getX(), point.getY());
        }
    }

    // ---------------------------------------------------------------------
    // ИЕРАРХИЧЕСКИЙ ПОИСК ПО ДИАПАЗОНУ X
    // ---------------------------------------------------------------------

    /**
     * Иерархический поиск точек пользователя по диапазону X.
     * <p>
     * Проходится по дереву Пользователь → Функции → Точки и
     * выбираются только те точки, для которых X попадает в
     * заданный диапазон.
     *
     * @param userId идентификатор пользователя
     * @param minX   нижняя граница X (включительно)
     * @param maxX   верхняя граница X (включительно)
     * @return список подходящих точек, отсортированных по X и индексу
     */
    public List<PointEntity> hierarchicalSearchPointsByX(long userId,
                                                         double minX,
                                                         double maxX) {
        log.info("Иерархический поиск точек пользователя id={} по X в [{}, {}]",
                userId, minX, maxX);

        List<FunctionEntity> functions = functionRepository.findByOwner_Id(userId);
        List<PointEntity> result = new ArrayList<>();

        for (FunctionEntity function : functions) {
            List<PointEntity> points =
                    pointRepository.findByFunction_IdOrderByIndex(function.getId());

            for (PointEntity point : points) {
                if (point.getX() >= minX && point.getX() <= maxX) {
                    result.add(point);
                    log.trace("Подходящая точка: funcId={} pointId={} x={} y={}",
                            function.getId(), point.getId(), point.getX(), point.getY());
                }
            }
        }

        result.sort(Comparator
                .comparingDouble(PointEntity::getX)
                .thenComparingInt(PointEntity::getIndex));

        log.info("Иерархический поиск завершён, найдено {} точек", result.size());
        return result;
    }

    // ---------------------------------------------------------------------
    // ПРАВА ДОСТУПА
    // ---------------------------------------------------------------------

    /**
     * Получить все права доступа пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список прав
     */
    public List<PermissionEntity> findPermissionsForUser(long userId) {
        log.info("Поиск прав доступа пользователя id={}", userId);

        List<PermissionEntity> perms = permissionRepository.findByUser_Id(userId);
        log.debug("Для пользователя id={} найдено {} прав", userId, perms.size());
        return perms;
    }
}
