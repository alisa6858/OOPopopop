package ru.lab5.manual.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис, реализующий разные варианты поиска по данным,
 * полученным из JDBC-DAO.
 *
 * Поддерживаются:
 *  - одиночный поиск;
 *  - множественный поиск с фильтрами и сортировкой;
 *  - обход в глубину;
 *  - обход в ширину;
 *  - иерархический поиск по дереву
 *    Пользователь → Функции → Точки.
 */
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final UserDao userDao;
    private final FunctionDao functionDao;
    private final PointDao pointDao;
    private final PermissionDao permissionDao;
    private final RecordToDtoMapper mapper;

    public SearchService(UserDao userDao,
                         FunctionDao functionDao,
                         PointDao pointDao,
                         PermissionDao permissionDao,
                         RecordToDtoMapper mapper) {
        this.userDao = Objects.requireNonNull(userDao, "userDao");
        this.functionDao = Objects.requireNonNull(functionDao, "functionDao");
        this.pointDao = Objects.requireNonNull(pointDao, "pointDao");
        this.permissionDao = Objects.requireNonNull(permissionDao, "permissionDao");
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    // ---------------------------------------------------------------------
    // ОДИНОЧНЫЙ ПОИСК
    // ---------------------------------------------------------------------

    /**
     * Одиночный поиск пользователя по точному совпадению имени.
     *
     * @param username имя пользователя
     * @return список из одного элемента, если пользователь найден,
     * либо пустой список.
     */
    public List<UserDto> findUserByUsername(String username) {
        log.info("Одиночный поиск пользователя по имени: {}", username);
        if (username == null || username.isBlank()) {
            log.warn("Пустое имя пользователя в одиночном поиске");
            return List.of();
        }

        Optional<UserRecord> recordOpt = userDao.findByUsername(username.trim());
        if (recordOpt.isEmpty()) {
            log.info("Пользователь с именем {} не найден", username);
            return List.of();
        }

        UserDto dto = mapper.toUserDto(recordOpt.get());
        log.debug("Найден пользователь: {}", dto);
        return List.of(dto);
    }

    // ---------------------------------------------------------------------
    // МНОЖЕСТВЕННЫЙ ПОИСК С ФИЛЬТРАМИ И СОРТИРОВКОЙ
    // ---------------------------------------------------------------------

    /**
     * Множественный поиск пользователей с возможностью фильтрации
     * по фрагменту имени и роли, а также сортировкой по выбранному полю.
     *
     * @param usernamePart часть имени (может быть null)
     * @param role         роль (может быть null, тогда роль не учитывается)
     * @param sortField    поле сортировки
     * @param ascending    направление сортировки: true — по возрастанию
     * @return список найденных пользователей.
     */
    public List<UserDto> searchUsers(String usernamePart,
                                     Integer role,
                                     SearchSortField sortField,
                                     boolean ascending) {
        log.info("Множественный поиск пользователей. Фрагмент имени='{}', роль={}, сортировка={}, ascending={}",
                usernamePart, role, sortField, ascending);

        List<UserRecord> all = userDao.findAll();
        log.debug("Всего пользователей в БД: {}", all.size());

        // фильтрация
        var stream = all.stream();

        if (usernamePart != null && !usernamePart.isBlank()) {
            String pattern = usernamePart.toLowerCase();
            stream = stream.filter(u -> u.getUsername() != null &&
                    u.getUsername().toLowerCase().contains(pattern));
        }

        if (role != null) {
            stream = stream.filter(u -> u.getRole() == role);
        }

        // сортировка
        Comparator<UserRecord> comparator;
        if (sortField == SearchSortField.ROLE) {
            comparator = Comparator.comparingInt(UserRecord::getRole)
                    .thenComparing(UserRecord::getUsername, Comparator.nullsLast(String::compareToIgnoreCase));
        } else {
            // по умолчанию сортируем по имени
            comparator = Comparator.comparing(UserRecord::getUsername,
                    Comparator.nullsLast(String::compareToIgnoreCase));
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        List<UserRecord> filtered = stream
                .sorted(comparator)
                .collect(Collectors.toList());

        log.debug("После фильтрации и сортировки осталось {} пользователей", filtered.size());

        return mapper.toUserDtoList(filtered);
    }

    // ---------------------------------------------------------------------
    // ПОИСК В ШИРИНУ ПО ИЕРАРХИИ
    // ---------------------------------------------------------------------

    /**
     * Обход в ширину по иерархии Пользователь → Функции → Точки.
     * Возвращает все точки пользователя, которые найдены в порядке BFS.
     *
     * @param userId идентификатор пользователя
     * @return список точек в порядке обхода в ширину.
     */
    public List<PointDto> breadthFirstPoints(long userId) {
        log.info("Запуск поиска в ширину для точек пользователя id={}", userId);

        List<PointDto> result = new ArrayList<>();

        // Корень дерева — пользователь, от него идут функции
        List<FunctionRecord> functions = functionDao.findByUserId(userId);
        log.debug("Для пользователя id={} найдено {} функций", userId, functions.size());

        Deque<FunctionRecord> queue = new ArrayDeque<>(functions);

        while (!queue.isEmpty()) {
            FunctionRecord currentFunction = queue.removeFirst();
            log.debug("Обрабатывается функция id={} name={}", currentFunction.getId(), currentFunction.getName());

            // для каждой функции получаем её точки
            List<PointRecord> points = pointDao.findByFunctionId(currentFunction.getId());
            log.debug("Для функции id={} найдено {} точек", currentFunction.getId(), points.size());

            List<PointDto> pointDtos = mapper.toPointDtoList(points);
            result.addAll(pointDtos);
        }

        log.info("Поиск в ширину завершён. Найдено {} точек", result.size());
        return result;
    }

    // ---------------------------------------------------------------------
    // ПОИСК В ГЛУБИНУ ПО ИЕРАРХИИ
    // ---------------------------------------------------------------------

    /**
     * Обход в глубину по той же иерархии Пользователь → Функции → Точки.
     * Порядок будет отличаться от breadthFirstPoints.
     *
     * @param userId идентификатор пользователя
     * @return список точек в порядке DFS.
     */
    public List<PointDto> depthFirstPoints(long userId) {
        log.info("Запуск поиска в глубину для точек пользователя id={}", userId);

        List<PointDto> result = new ArrayList<>();
        List<FunctionRecord> functions = functionDao.findByUserId(userId);

        log.debug("Для пользователя id={} найдено {} функций", userId, functions.size());

        for (FunctionRecord function : functions) {
            dfsForFunction(function, result);
        }

        log.info("Поиск в глубину завершён. Найдено {} точек", result.size());
        return result;
    }

    /**
     * Вспомогательный рекурсивный метод DFS для одной функции.
     */
    private void dfsForFunction(FunctionRecord function, List<PointDto> accumulator) {
        log.debug("DFS: функция id={} name={}", function.getId(), function.getName());

        List<PointRecord> points = pointDao.findByFunctionId(function.getId());
        for (PointRecord point : points) {
            PointDto dto = mapper.toPointDto(point);
            accumulator.add(dto);
            log.trace("DFS: добавлена точка id={} index={} x={} y={}",
                    dto.getId(), dto.getIndex(), dto.getX(), dto.getY());
        }
    }

    // ---------------------------------------------------------------------
    // ИЕРАРХИЧЕСКИЙ ПОИСК С ФИЛЬТРАЦИЕЙ ПО ДИАПАЗОНУ X
    // ---------------------------------------------------------------------

    /**
     * Иерархический поиск точек пользователя по диапазону X.
     * Проходится по дереву Пользователь → Функции → Точки,
     * выбирая только те точки, у которых X лежит в указанном диапазоне.
     *
     * @param userId идентификатор пользователя
     * @param minX   нижняя граница X (включительно)
     * @param maxX   верхняя граница X (включительно)
     * @return список подходящих точек, отсортированных по X.
     */
    public List<PointDto> hierarchicalSearchPointsByX(long userId, double minX, double maxX) {
        log.info("Иерархический поиск точек пользователя id={} по диапазону X: [{}, {}]",
                userId, minX, maxX);

        List<FunctionRecord> functions = functionDao.findByUserId(userId);
        List<PointDto> result = new ArrayList<>();

        for (FunctionRecord function : functions) {
            List<PointRecord> points = pointDao.findByFunctionId(function.getId());
            for (PointRecord point : points) {
                if (point.getX() >= minX && point.getX() <= maxX) {
                    PointDto dto = mapper.toPointDto(point);
                    result.add(dto);
                    log.trace("Подходящая точка: функция id={}, pointId={}, x={}, y={}",
                            function.getId(), dto.getId(), dto.getX(), dto.getY());
                }
            }
        }

        // сортируем по X, при равенстве X — по индексу
        result.sort(Comparator
                .comparingDouble(PointDto::getX)
                .thenComparingInt(PointDto::getIndex));

        log.info("Иерархический поиск завершён. Найдено {} точек", result.size());
        return result;
    }

    // ---------------------------------------------------------------------
    // Пример поиска прав доступа (множественный поиск по пользователю)
    // ---------------------------------------------------------------------

    /**
     * Получить все права доступа пользователя и вернуть их в виде DTO.
     *
     * @param userId идентификатор пользователя
     * @return список прав доступа.
     */
    public List<PermissionDto> findPermissionsForUser(long userId) {
        log.info("Поиск прав доступа пользователя id={}", userId);

        List<PermissionRecord> records = permissionDao.findByUserId(userId);
        log.debug("Для пользователя id={} найдено {} прав", userId, records.size());

        return mapper.toPermissionDtoList(records);
    }
}
