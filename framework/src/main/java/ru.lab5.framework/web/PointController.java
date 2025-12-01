package ru.lab5.framework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    private final AccessControlApi accessControlApi;

    public PointController(AccessControlApi accessControlApi) {
        this.accessControlApi = accessControlApi;
    }

    /**
     * Получить точку по идентификатору.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PointDto> getById(@PathVariable Long id) {
        log.info("GET /api/points/{} – запрос точки по id", id);
        return accessControlApi.getPointById(id)
                .map(point -> {
                    log.debug("Точка найдена: id={}, functionId={}", point.getId(), point.getFunctionId());
                    return ResponseEntity.ok(point);
                })
                .orElseGet(() -> {
                    log.warn("Точка с id={} не найдена", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Получить точки по функции.
     */
    @GetMapping
    public List<PointDto> getByFunction(@RequestParam Long functionId) {
        log.info("GET /api/points – запрос точек по functionId={}", functionId);
        List<PointDto> list = accessControlApi.getPointsByFunctionId(functionId);
        log.debug("Найдено точек: {}", list.size());
        return list;
    }

    /**
     * Создать точку.
     */
    @PostMapping
    public PointDto create(@RequestBody PointDto pointDto) {
        log.info("POST /api/points – создание точки");
        log.debug("Тело запроса: functionId={}, name={}",
                pointDto.getFunctionId(),
                pointDto.getName());
        PointDto created = accessControlApi.createPoint(pointDto);
        log.debug("Точка создана: id={}", created.getId());
        return created;
    }

    /**
     * Обновить точку.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointDto> update(@PathVariable Long id,
                                           @RequestBody PointDto pointDto) {
        log.info("PUT /api/points/{} – обновление точки", id);
        return accessControlApi.updatePoint(id, pointDto)
                .map(updated -> {
                    log.debug("Точка обновлена: id={}, functionId={}", updated.getId(), updated.getFunctionId());
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    log.warn("Не удалось обновить точку: id={} не найден", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Удалить точку.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/points/{} – удаление точки", id);
        boolean deleted = accessControlApi.deletePoint(id);
        if (deleted) {
            log.debug("Точка с id={} удалена", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("Удаление точки: id={} не найден", id);
        return ResponseEntity.notFound().build();
    }
}
