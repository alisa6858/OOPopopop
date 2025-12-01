package ru.lab5.manual.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lab5.manual.dao.UserDao;
import ru.lab5.manual.dao.jdbc.JdbcUserDao;
import ru.lab5.manual.dto.UserDto;
import ru.lab5.manual.dto.mapper.RecordToDtoMapper;
import ru.lab5.manual.model.UserRecord;

import java.util.Optional;

/**
 * Сервис аутентификации для manual-ветки.
 * Работает через JDBC-DAO и маппер в UserDto.
 */
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserDao userDao;
    private final RecordToDtoMapper mapper;

    public AuthService() {
        this.userDao = new JdbcUserDao();
        this.mapper = new RecordToDtoMapper();
    }

    /**
     * Проверка логина и пароля.
     *
     * @param username логин
     * @param password пароль в открытом виде (для лабораторной)
     * @return Optional с UserDto, если всё ок, иначе Optional.empty()
     */
    public Optional<UserDto> authenticate(String username, String password) {
        log.debug("Попытка аутентификации: {}", username);

        Optional<UserRecord> recordOpt = userDao.findByUsername(username);
        if (recordOpt.isEmpty()) {
            log.info("Пользователь не найден: {}", username);
            return Optional.empty();
        }

        UserRecord record = recordOpt.get();

        if (!record.getPassword().equals(password)) {
            log.info("Неверный пароль для пользователя: {}", username);
            return Optional.empty();
        }

        UserDto dto = mapper.toUserDto(record);
        log.info("Аутентификация успешна: {}", username);
        return Optional.of(dto);
    }
}
