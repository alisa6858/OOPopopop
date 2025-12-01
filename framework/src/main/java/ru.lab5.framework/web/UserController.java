package ru.lab5.framework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lab5.api.*;

import java.util.List;

/**
 * REST-контроллер для работы с пользователями.
 * Все операции проходят через общий контракт {@link AccessControlApi}.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final AccessControlApi accessControlApi;

    public UserController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить пользователя по идентификатору.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        log.info("GET /api/users/{} – запрос пользователя по id", id);
        return accessControlApi.getUserById(id)
                .map(user -> {
                    log.debug("Пользователь найден: id={}, username={}", user.getId(), user.getUsername());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    log.warn("Пользователь с id={} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Поиск пользователей по фильтрам, сортировке и пагинации.
     */
    @GetMapping
    public List<UserDto> findUsers(
            @RequestParam(required = false) String usernameLike,
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) SortField sortField,
            @RequestParam(required = false) SortDirection sortDirection,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        log.info("GET /api/users – поиск пользователей");
        log.debug("Параметры поиска: usernameLike={}, role={}, sortField={}, sortDirection={}, page={}, size={}",
                usernameLike, role, sortField, sortDirection, page, size);

        UserSearchRequest request = new UserSearchRequest();
        request.setUsernameLike(usernameLike);
        request.setRoleEquals(role);
        request.setSortField(sortField);
        request.setSortDirection(sortDirection);
        request.setPage(new PageRequest(page, size));

        List<UserDto> list = accessControlApi.findUsers(request);
        log.debug("Результат поиска: найдено пользователей={}", list.size());
        return list;
    }

    /**
     * Создать нового пользователя.
     */
    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("POST /api/users – создание пользователя");
        log.debug("Тело запроса: username={}, role={}", userDto.getUsername(), userDto.getRole());
        UserDto created = accessControlApi.createUser(userDto);
        log.debug("Пользователь создан: id={}", created.getId());
        return created;
    }

    /**
     * Обновить пользователя.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @RequestBody UserDto userDto) {
        log.info("PUT /api/users/{} – обновление пользователя", id);
        return accessControlApi.updateUser(id, userDto)
                .map(updated -> {
                    log.debug("Пользователь обновлён: id={}, username={}", updated.getId(), updated.getUsername());
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    log.warn("Не удалось обновить пользователя: id={} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Удалить пользователя.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/users/{} – удаление пользователя", id);
        boolean deleted = accessControlApi.deleteUser(id);
        if (deleted) {
            log.debug("Пользователь с id={} удалён", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("Удаление пользователя: id={} не найден", id);
        return ResponseEntity.notFound().build();
    }
}
