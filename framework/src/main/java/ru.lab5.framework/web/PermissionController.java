package ru.lab5.framework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lab5.api.AccessControlApi;
import ru.lab5.api.PermissionDto;

import java.util.List;

/**
 * REST-контроллер для работы с правами доступа.
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);

    private final AccessControlApi accessControlApi;

    public PermissionController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить список прав по пользователю.
     */
    @GetMapping
    public List<PermissionDto> getByUser(@RequestParam Long userId) {
        log.info("GET /api/permissions – запрос прав пользователя userId={}", userId);
        List<PermissionDto> list = accessControlApi.getPermissionsByUserId(userId);
        log.debug("Найдено прав: {}", list.size());
        return list;
    }

    /**
     * Выдать право доступа.
     */
    @PostMapping
    public PermissionDto grant(@RequestBody PermissionDto permissionDto) {
        log.info("POST /api/permissions – выдача права доступа");
        log.debug("Тело запроса: userId={}, functionId={}, allowed={}",
                permissionDto.getUserId(),
                permissionDto.getFunctionId(),
                permissionDto.isAllowed());
        PermissionDto created = accessControlApi.grantPermission(permissionDto);
        log.debug("Право выдано: id={}", created.getId());
        return created;
    }

    /**
     * Отозвать право доступа.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Long id) {
        log.info("DELETE /api/permissions/{} – отзыв права доступа", id);
        boolean revoked = accessControlApi.revokePermission(id);
        if (revoked) {
            log.debug("Право доступа с id={} отозвано", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("Отзыв права: id={} не найден", id);
        return ResponseEntity.notFound().build();
    }
}
