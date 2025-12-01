package ru.lab5.framework.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lab5.api.AccessControlApi;
import ru.lab5.api.PointDto;

import java.util.List;

/**
 * REST-контроллер для работы с точками внутри функций.
 */
@RestController
@RequestMapping("/api/points")
public class PointController {

    private final AccessControlApi accessControlApi;

    public PointController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить точку по id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PointDto> getById(@PathVariable Long id) {
        return accessControlApi.getPointById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Получить точки по id функции.
     */
    @GetMapping
    public List<PointDto> getByFunction(@RequestParam Long functionId) {
        return accessControlApi.getPointsByFunctionId(functionId);
    }

    /**
     * Создать точку.
     */
    @PostMapping
    public PointDto create(@RequestBody PointDto pointDto) {
        return accessControlApi.createPoint(pointDto);
    }

    /**
     * Обновить точку.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointDto> update(@PathVariable Long id,
                                           @RequestBody PointDto pointDto) {
        return accessControlApi.updatePoint(id, pointDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Удалить точку.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = accessControlApi.deletePoint(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
