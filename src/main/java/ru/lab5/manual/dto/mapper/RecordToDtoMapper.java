package ru.lab5.manual.dto.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab5.manual.dto.FunctionDto;
import ru.lab5.manual.dto.PermissionDto;
import ru.lab5.manual.dto.PointDto;
import ru.lab5.manual.dto.UserDto;
import ru.lab5.manual.model.FunctionRecord;
import ru.lab5.manual.model.PermissionRecord;
import ru.lab5.manual.model.PointRecord;
import ru.lab5.manual.model.UserRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Простейший маппер из Record-объектов, которые возвращает DAO,
 * в DTO-объекты для передачи данных наружу.
 */
public class RecordToDtoMapper {

    private static final Logger log = LoggerFactory.getLogger(RecordToDtoMapper.class);

    public UserDto toUserDto(UserRecord record) {
        if (record == null) {
            log.warn("Попытка преобразовать null в UserDto");
            return null;
        }
        log.debug("Преобразование UserRecord id={} в UserDto", record.getId());
        return new UserDto()
                .setId(record.getId())
                .setUsername(record.getUsername())
                .setRole(record.getRole());
    }

    public List<UserDto> toUserDtoList(List<UserRecord> records) {
        log.debug("Преобразование списка пользователей размером {} в UserDto",
                records == null ? 0 : records.size());
        if (records == null) {
            return List.of();
        }
        return records.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    public FunctionDto toFunctionDto(FunctionRecord record) {
        if (record == null) {
            log.warn("Попытка преобразовать null в FunctionDto");
            return null;
        }
        log.debug("Преобразование FunctionRecord id={} в FunctionDto", record.getId());
        return new FunctionDto()
                .setId(record.getId())
                .setOwnerId(record.getUserId())
                .setName(record.getName())
                .setDescription(record.getDescription());
    }

    public List<FunctionDto> toFunctionDtoList(List<FunctionRecord> records) {
        log.debug("Преобразование списка функций размером {} в FunctionDto",
                records == null ? 0 : records.size());

        if (records == null) {
            return List.of();
        }

        return records.stream()
                .map(this::toFunctionDto)
                .collect(Collectors.toList());
    }

    public PointDto toPointDto(PointRecord record) {
        if (record == null) {
            log.warn("Попытка преобразовать null в PointDto");
            return null;
        }
        log.debug("Преобразование PointRecord id={} в PointDto", record.getId());
        return new PointDto()
                .setId(record.getId())
                .setFunctionId(record.getFunctionId())
                .setIndex(record.getIndex())
                .setX(record.getX())
                .setY(record.getY());
    }

    public List<PointDto> toPointDtoList(List<PointRecord> records) {
        log.debug("Преобразование списка точек размером {} в PointDto",
                records == null ? 0 : records.size());
        if (records == null) {
            return List.of();
        }
        return records.stream()
                .map(this::toPointDto)
                .collect(Collectors.toList());
    }

    public PermissionDto toPermissionDto(PermissionRecord record) {
        if (record == null) {
            log.warn("Попытка преобразовать null в PermissionDto");
            return null;
        }
        log.debug("Преобразование PermissionRecord id={} в PermissionDto", record.getId());
        return new PermissionDto()
                .setId(record.getId())
                .setUserId(record.getUserId())
                .setFunctionId(record.getFunctionId())
                .setAccess(record.getAccess());
    }

    public List<PermissionDto> toPermissionDtoList(List<PermissionRecord> records) {
        log.debug("Преобразование списка разрешений размером {} в PermissionDto",
                records == null ? 0 : records.size());
        if (records == null) {
            return List.of();
        }
        return records.stream()
                .map(this::toPermissionDto)
                .collect(Collectors.toList());
    }
}
