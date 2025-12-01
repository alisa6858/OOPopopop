package ru.lab5.framework.web;

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

    private final AccessControlApi accessControlApi;

    public PermissionController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить права пользователя.
     */
    @GetMapping
    public List<PermissionDto> getByUser(@RequestParam Long userId) {
        return accessControlApi.getPermissionsByUserId(userId);
    }

    /**
     * Выдать право.
     */
    @PostMapping
    public PermissionDto grant(@RequestBody PermissionDto permissionDto) {
        return accessControlApi.grantPermission(permissionDto);
    }

    /**
     * Отозвать право.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Long id) {
        boolean revoked = accessControlApi.revokePermission(id);
        if (revoked) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
