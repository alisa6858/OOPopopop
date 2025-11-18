package ru.lab5.manual.service;

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

import java.util.List;

/**
 * Сервис чтения данных и трансформации их в DTO.
 * <p>
 * Берёт данные из DAO-интерфейсов и с помощью маппера
 * преобразует их в объекты DTO.
 */
public class DtoReadService {

    private static final Logger log = LoggerFactory.getLogger(DtoReadService.class);

    private final UserDao userDao;
    private final FunctionDao functionDao;
    private final PointDao pointDao;
    private final PermissionDao permissionDao;
    private final RecordToDtoMapper mapper;

    public DtoReadService(UserDao userDao,
                          FunctionDao functionDao,
                          PointDao pointDao,
                          PermissionDao permissionDao,
                          RecordToDtoMapper mapper) {
        this.userDao = userDao;
        this.functionDao = functionDao;
        this.pointDao = pointDao;
        this.permissionDao = permissionDao;
        this.mapper = mapper;
    }

    public List<UserDto> loadAllUsers() {
        log.info("Загрузка всех пользователей и преобразование в DTO");
        List<UserRecord> records = userDao.findAll();
        return mapper.toUserDtoList(records);
    }

    public List<FunctionDto> loadAllFunctions() {
        log.info("Загрузка всех функций и преобразование в DTO");
        List<FunctionRecord> records = functionDao.findAll();
        return mapper.toFunctionDtoList(records);
    }

    public List<PointDto> loadPointsForFunction(long functionId) {
        log.info("Загрузка точек для функции id={} и преобразование в DTO", functionId);
        List<PointRecord> records = pointDao.findByFunctionId(functionId);
        return mapper.toPointDtoList(records);
    }

    public List<PermissionDto> loadPermissionsForUser(long userId) {
        log.info("Загрузка прав доступа пользователя id={} и преобразование в DTO", userId);
        List<PermissionRecord> records = permissionDao.findByUserId(userId);
        return mapper.toPermissionDtoList(records);
    }
}
