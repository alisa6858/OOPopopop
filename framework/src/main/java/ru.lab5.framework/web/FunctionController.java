package ru.lab5.framework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lab5.api.AccessControlApi;
import ru.lab5.api.FunctionDto;

import java.util.List;

/**
 * REST-контроллер для работы с функциями.
 * Оперирует только через унифицированный контракт {@link AccessControlApi}.
 */
@RestController
@RequestMapping("/api/functions")
public class FunctionController {

    private static final Logger log = LoggerFactory.getLogger(FunctionController.class);

    private final AccessControlApi accessControlApi;

    public FunctionController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить функцию по идентификатору.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FunctionDto> getById(@PathVariable Long id) {
        log.info("GET /api/functions/{} – запрос функции по id", id);
        return accessControlApi.getFunctionById(id)
                .map(function -> {
                    log.debug("Функция найдена: id={}, code={}", function.getId(), function.getCode());
                    return ResponseEntity.ok(function);
                })
                .orElseGet(() -> {
                    log.warn("Функция с id={} не найдена", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Получить все функции без фильтрации.
     */
    @GetMapping
    public List<FunctionDto> getAll() {
        log.info("GET /api/functions – запрос списка всех функций");
        List<FunctionDto> list = accessControlApi.getAllFunctions();
        log.debug("Найдено функций: {}", list.size());
        return list;
    }

    /**
     * Создать новую функцию.
     */
    @PostMapping
    public FunctionDto create(@RequestBody FunctionDto functionDto) {
        log.info("POST /api/functions – создание новой функции");
        log.debug("Тело запроса: code={}, name={}", functionDto.getCode(), functionDto.getName());
        FunctionDto created = accessControlApi.createFunction(functionDto);
        log.debug("Функция создана: id={}", created.getId());
        return created;
    }

    /**
     * Обновить функцию.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FunctionDto> update(@PathVariable Long id,
                                              @RequestBody FunctionDto functionDto) {
        log.info("PUT /api/functions/{} – обновление функции", id);
        return accessControlApi.updateFunction(id, functionDto)
                .map(updated -> {
                    log.debug("Функция обновлена: id={}, code={}", updated.getId(), updated.getCode());
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    log.warn("Не удалось обновить функцию: id={} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Удалить функцию.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/functions/{} – удаление функции", id);
        boolean deleted = accessControlApi.deleteFunction(id);
        if (deleted) {
            log.debug("Функция с id={} удалена", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("Удаление функции: id={} не найден", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Обход структуры функций в глубину.
     */
    @GetMapping("/{id}/depth-first")
    public List<FunctionDto> traverseDepthFirst(@PathVariable Long id) {
        log.info("GET /api/functions/{}/depth-first – обход функций в глубину", id);
        List<FunctionDto> list = accessControlApi.traverseFunctionsDepthFirst(id);
        log.debug("Результат обхода в глубину: элементов={}", list.size());
        return list;
    }

    /**
     * Обход структуры функций в ширину.
     */
    @GetMapping("/{id}/breadth-first")
    public List<FunctionDto> traverseBreadthFirst(@PathVariable Long id) {
        log.info("GET /api/functions/{}/breadth-first – обход функций в ширину", id);
        List<FunctionDto> list = accessControlApi.traverseFunctionsBreadthFirst(id);
        log.debug("Результат обхода в ширину: элементов={}", list.size());
        return list;
    }

    /**
     * Загрузка иерархии функций, связанных с указанным корнем.
     */
    @GetMapping("/{id}/hierarchy")
    public List<FunctionDto> loadHierarchy(@PathVariable Long id) {
        log.info("GET /api/functions/{}/hierarchy – запрос иерархии функций", id);
        List<FunctionDto> list = accessControlApi.loadFunctionHierarchy(id);
        log.debug("Иерархия функций: элементов={}", list.size());
        return list;
    }
}
