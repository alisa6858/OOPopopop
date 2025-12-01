package ru.lab5.framework.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lab5.api.*;

import java.util.List;

/**
 * REST-контроллер для работы с пользователями.
 * Все операции построены на общем контракте AccessControlApi.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AccessControlApi accessControlApi;

    public UserController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить пользователя по id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return accessControlApi.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Найти пользователей с учётом фильтров и сортировки.
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
        UserSearchRequest request = new UserSearchRequest();
        request.setUsernameLike(usernameLike);
        request.setRoleEquals(role);
        request.setSortField(sortField);
        request.setSortDirection(sortDirection);

        PageRequest pageRequest = new PageRequest(page, size);
        request.setPage(pageRequest);

        return accessControlApi.findUsers(request);
    }

    /**
     * Создать нового пользователя.
     */
    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return accessControlApi.createUser(userDto);
    }

    /**
     * Обновить данные пользователя.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @RequestBody UserDto userDto) {
        return accessControlApi.updateUser(id, userDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Удалить пользователя.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = accessControlApi.deleteUser(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
