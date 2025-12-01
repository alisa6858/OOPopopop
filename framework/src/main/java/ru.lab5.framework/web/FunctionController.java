package ru.lab5.framework.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lab5.api.AccessControlApi;
import ru.lab5.api.FunctionDto;

import java.util.List;

/**
 * REST-контроллер для работы с функциями.
 */
@RestController
@RequestMapping("/api/functions")
public class FunctionController {

    private final AccessControlApi accessControlApi;

    public FunctionController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить функцию по id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FunctionDto> getById(@PathVariable Long id) {
        return accessControlApi.getFunctionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Получить все функции.
     */
    @GetMapping
    public List<FunctionDto> getAll() {
        return accessControlApi.getAllFunctions();
    }

    /**
     * Создать функцию.
     */
    @PostMapping
    public FunctionDto create(@RequestBody FunctionDto functionDto) {
        return accessControlApi.createFunction(functionDto);
    }

    /**
     * Обновить функцию.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FunctionDto> update(@PathVariable Long id,
                                              @RequestBody FunctionDto functionDto) {
        return accessControlApi.updateFunction(id, functionDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Удалить функцию.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = accessControlApi.deleteFunction(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
